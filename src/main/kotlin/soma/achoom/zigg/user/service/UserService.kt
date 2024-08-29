package soma.achoom.zigg.user.service


import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.auth.filter.CustomUserDetails
import soma.achoom.zigg.storage.GCSService
import soma.achoom.zigg.user.dto.UserRequestDto
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.exception.NicknameUserNotFoundException
import soma.achoom.zigg.user.exception.UserAlreadyExistsException
import soma.achoom.zigg.user.exception.UserNotFoundException
import soma.achoom.zigg.user.repository.UserRepository


@Service
class UserService(
    private val gcsService: GCSService,
    private val userRepository: UserRepository
) {

    fun searchUser(authentication: Authentication, nickname: String): MutableSet<UserResponseDto> {
        val users = userRepository.findUsersByUserNicknameLike(nickname,PageRequest.of(0,5))
            .let { it.ifEmpty { throw NicknameUserNotFoundException() } }
            .filter { it.userNickname != authenticationToUser(authentication).userNickname }

        return users.map {
            UserResponseDto(
                userId = it.userId,
                userName = it.userName,
                userNickname = it.userNickname,
                profileImageUrl = gcsService.getPreSignedGetUrl(it.profileImageKey!!)
            )
        }.toMutableSet()
    }

    fun getUserInfo(authentication: Authentication): UserResponseDto {
        val user = authenticationToUser(authentication)
        return UserResponseDto(
            userId = user.userId,
            userName = user.userName,
            userNickname = user.userNickname,
            profileImageUrl = gcsService.getPreSignedGetUrl(user.profileImageKey!!)
        )
    }

    fun updateUser(authentication: Authentication, userRequestDto: UserRequestDto): UserResponseDto {
        userRequestDto.userNickname?.let {
            userRepository.findUserByUserNickname(it)?.let {

                throw UserAlreadyExistsException()
            }
        }
        val user = authenticationToUser(authentication)
        user.userName = userRequestDto.userName
        user.userNickname = userRequestDto.userNickname
        userRepository.save(user)
        return UserResponseDto(
            userId = user.userId,
            userName = user.userName,
            userNickname = user.userNickname,
            profileImageUrl = gcsService.getPreSignedGetUrl(user.profileImageKey!!)
        )
    }
    fun authenticationToUser(authentication: Authentication): User {
        val providerId = authentication.name
        val userDetails = authentication.principal as CustomUserDetails

        val user = userRepository.findUserByPlatformAndProviderId(
            OAuthProviderEnum.valueOf(userDetails.getOAuthProvider()), providerId
        ) ?: throw IllegalArgumentException("user not found")
        return user
    }
    fun findUserByNickName(nickname: String): User {
        val user =  userRepository.findUserByUserNickname(nickname)?: throw UserNotFoundException()
        return user
    }

}