package soma.achoom.zigg.auth.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.auth.dto.*
import soma.achoom.zigg.auth.filter.JwtTokenProvider
import soma.achoom.zigg.content.repository.ImageRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.entity.UserRole
import soma.achoom.zigg.user.exception.UserAlreadyExistsException
import soma.achoom.zigg.user.repository.UserRepository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class AuthenticationService @Autowired constructor(
    private var jwtTokenProvider: JwtTokenProvider,
    @Value("\${user.default.profile.images}")
    private val defaultProfileImages: List<String>,
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository
)  {
    @Transactional(readOnly = true)
    fun userExistsCheckByOAuthPlatformAndProviderId(oAuth2MetaDataRequestDto: OAuth2MetaDataRequestDto): Boolean {
        runCatching {
            userRepository.findUserByPlatformAndProviderId(
                OAuthProviderEnum.valueOf(oAuth2MetaDataRequestDto.platform), oAuth2MetaDataRequestDto.providerId
            ) ?: return false
        }.onFailure {

            return false
        }
        return true
    }
    @Transactional(readOnly = true)
    fun checkNickname(nicknameRequestDto:NicknameValidRequestDto): NicknameValidResponseDto {
        return NicknameValidResponseDto(userRepository.existsUserByNickname(nicknameRequestDto.nickname))
    }
    fun generateJWTToken(oAuth2UserRequestDto: OAuth2UserRequestDto): HttpHeaders {
        val user = userRepository.findUserByPlatformAndProviderId(
            OAuthProviderEnum.valueOf(oAuth2UserRequestDto.platform), oAuth2UserRequestDto.providerId
        ) ?: throw IllegalArgumentException("register first")
        val accessToken = jwtTokenProvider.createTokenWithUserInfo(user)
        val header = HttpHeaders()
        header.set("Authorization", accessToken)
        header.set("platform", oAuth2UserRequestDto.platform)
        user.jwtToken = accessToken
        userRepository.save(user)

        return header
    }
    @Transactional(readOnly = false)
    fun registers(oAuth2UserRequestDto: OAuth2UserRequestDto): HttpHeaders {
        oAuth2UserRequestDto.userNickname?: throw IllegalArgumentException("userNickname is required")
        userRepository.findUserByNickname(oAuth2UserRequestDto.userNickname)?.let {
            throw UserAlreadyExistsException()
        }
        when (oAuth2UserRequestDto.platform) {
            OAuthProviderEnum.GOOGLE.name -> {
                val user = saveOrUpdate(oAuth2UserRequestDto)

                if (verifyGoogleToken(oAuth2UserRequestDto.idToken)) {
                    val accessToken = jwtTokenProvider.createTokenWithUserInfo(user)
                    val header = HttpHeaders()
                    header.set("Authorization", accessToken)
                    header.set("platform", oAuth2UserRequestDto.platform)
                    user.jwtToken = accessToken
                    userRepository.save(user)
                    return header
                } else {
                    throw IllegalArgumentException("Invalid access token")
                }
            }

            OAuthProviderEnum.KAKAO.name -> {
                if (verifyKakaoToken(oAuth2UserRequestDto.idToken)) {
                    val user = saveOrUpdate(oAuth2UserRequestDto)

                    val accessToken = jwtTokenProvider.createTokenWithUserInfo(user)
                    val header = HttpHeaders()
                    header.set("Authorization", accessToken)
                    header.set("platform", oAuth2UserRequestDto.platform)
                    user.jwtToken = accessToken
                    userRepository.save(user)
                    return header
                } else {
                    throw IllegalArgumentException("Invalid access token")
                }
            }

            OAuthProviderEnum.APPLE.name -> {
                val user = saveOrUpdate(oAuth2UserRequestDto)

                val accessToken = jwtTokenProvider.createTokenWithUserInfo(user)
                val header = HttpHeaders()
                header.set("Authorization", accessToken)
                header.set("platform", oAuth2UserRequestDto.platform)
                user.jwtToken = accessToken
                userRepository.save(user)

                return header
            }
            OAuthProviderEnum.GUEST.name -> {
                val user = saveOrUpdate(oAuth2UserRequestDto)

                val accessToken = jwtTokenProvider.createTokenWithUserInfo(user)
                val header = HttpHeaders()
                header.set("Authorization", accessToken)
                header.set("platform", oAuth2UserRequestDto.platform)
                user.jwtToken = accessToken
                userRepository.save(user)

                return header
            }

            else -> {
                throw IllegalArgumentException("Unsupported platform")
            }
        }

    }
    private fun verifyGoogleToken(idToken: String): Boolean {
        val client =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(java.time.Duration.ofSeconds(20)).build()

        val request =
            HttpRequest.newBuilder().uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=$idToken"))
                .timeout(java.time.Duration.ofSeconds(20)).GET().build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.statusCode() == 200
    }

    private fun verifyKakaoToken(idToken: String): Boolean {
        val client = HttpClient.newHttpClient()
        val tokenData = "id_token=$idToken"
        val request = HttpRequest.newBuilder().uri(URI.create("https://kauth.kakao.com/oauth/tokeninfo"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(tokenData)).build()


        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.statusCode() == 200
    }


    private fun saveOrUpdate(oAuth2UserRequestDto: OAuth2UserRequestDto): User {
        var user: User? = userRepository.findUserByPlatformAndProviderId(
            OAuthProviderEnum.valueOf(oAuth2UserRequestDto.platform), oAuth2UserRequestDto.providerId
        )
        if(user == null) {
            val image = imageRepository.findByImageKey(defaultProfileImages.random()) ?: throw RuntimeException("사진이 없습니다.")

            user = User(
                nickname = oAuth2UserRequestDto.userNickname,
                name = oAuth2UserRequestDto.userName,
                providerId = oAuth2UserRequestDto.providerId,
                platform = OAuthProviderEnum.valueOf(oAuth2UserRequestDto.platform),
                jwtToken = "",
                profileImageKey = image,
            )
        }

        if(user.platform == OAuthProviderEnum.GUEST){
            user.role = UserRole.GUEST
        }

        return userRepository.save(user)

    }

}