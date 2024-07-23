package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.auth.OAuthProviderEnum
import soma.achoom.zigg.v0.auth.JwtTokenProvider
import soma.achoom.zigg.v0.dto.token.OAuthTokenVerificationDto
import soma.achoom.zigg.v0.dto.token.TokenResponseDto
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.repository.UserRepository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.jvm.optionals.getOrElse

@Service
class AuthenticationService @Autowired constructor(
    private var jwtTokenProvider: JwtTokenProvider,
    private var userRepository: UserRepository

) {

    fun verifyAndGenerateToken(requestDto: OAuthTokenVerificationDto): TokenResponseDto {
        when {
            requestDto.platform == OAuthProviderEnum.GOOGLE.name -> {
                 if(verifyGoogleToken(requestDto.idToken)){
                     val user = saveOrUpdate(requestDto)
                     val accessToken = jwtTokenProvider.createTokenWithUserInfo(user,requestDto.userInfo)
                     return TokenResponseDto(accessToken)
                    }
                 else{
                     throw IllegalArgumentException("Invalid access token")
                 }
            }
            requestDto.platform == OAuthProviderEnum.KAKAO.name -> {

                if(verifyKakaoToken(requestDto.idToken)){
                    val user = saveOrUpdate(requestDto)
                    val accessToken = jwtTokenProvider.createTokenWithUserInfo(user,requestDto.userInfo)
                    return TokenResponseDto(accessToken)
                }
                else{
                    throw IllegalArgumentException("Invalid access token")
                }
            }
            requestDto.platform == OAuthProviderEnum.APPLE.name -> {

                val user = saveOrUpdate(requestDto)
                val accessToken = jwtTokenProvider.createTokenWithUserInfo(user, requestDto.userInfo)
                return TokenResponseDto(accessToken)
            }


            else -> {
                throw IllegalArgumentException("Unsupported platform")
            }
        }

    }
    private fun verifyGoogleToken(idToken: String): Boolean {
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(java.time.Duration.ofSeconds(20))
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=$idToken"))
            .timeout(java.time.Duration.ofSeconds(20))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.statusCode() == 200
    }

    private fun verifyKakaoToken(idToken: String): Boolean{
        val client = HttpClient.newHttpClient()
        val tokenData = "id_token=$idToken"
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://kauth.kakao.com/oauth/tokeninfo"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(tokenData))
            .build()


        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.statusCode() == 200
    }


    private fun saveOrUpdate(oAuthTokenVerificationDto: OAuthTokenVerificationDto): User {
        val user =  userRepository.findUserByProviderAndProviderId(
            OAuthProviderEnum.valueOf(oAuthTokenVerificationDto.platform),
            oAuthTokenVerificationDto.providerId!!
        ).getOrElse { User(providerId = oAuthTokenVerificationDto.providerId!!, provider = OAuthProviderEnum.valueOf(oAuthTokenVerificationDto.platform)) }
        return userRepository.save(user)

    }

}