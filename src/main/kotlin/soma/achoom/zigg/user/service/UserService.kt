package soma.achoom.zigg.user.service
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.auth.filter.CustomUserDetails
import soma.achoom.zigg.firebase.dto.FCMTokenRequestDto
import soma.achoom.zigg.firebase.service.FCMService
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.spaceuser.repository.SpaceUserRepository
import soma.achoom.zigg.user.dto.UserRequestDto
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.exception.UserAlreadyExistsException
import soma.achoom.zigg.user.exception.UserNotFoundException
import soma.achoom.zigg.user.repository.UserRepository


@Service
class UserService(
    private val userRepository: UserRepository,
    private val responseDtoManager: ResponseDtoManager,
    private val fcmService: FCMService,
    private val spaceUserRepository: SpaceUserRepository
) {
    @Transactional(readOnly = true)
    fun searchUser(authentication: Authentication, nickname: String): MutableSet<UserResponseDto> {
        val users = userRepository.findUsersByUserNicknameLike(nickname,PageRequest.of(0,5))

        return users
            .filter { it.userNickname != authenticationToUser(authentication).userNickname }.map {
            responseDtoManager.generateUserResponseDto(it)
        }.toMutableSet()
    }
    @Transactional(readOnly = true)
    fun getUserInfo(authentication: Authentication): UserResponseDto {
        val user = authenticationToUser(authentication)
        return responseDtoManager.generateUserResponseDto(user)

    }
    @Transactional(readOnly = false)
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
        return responseDtoManager.generateUserResponseDto(user)
    }
    @Transactional(readOnly = true)
    fun authenticationToUser(authentication: Authentication): User {
        val providerId = authentication.name
        val userDetails = authentication.principal as CustomUserDetails

        val user = userRepository.findUserByPlatformAndProviderId(
            OAuthProviderEnum.valueOf(userDetails.getOAuthProvider()), providerId
        ) ?: throw IllegalArgumentException("user not found")
        return user
    }
    @Transactional(readOnly = true)
    fun findUserByNickName(nickname: String): User {
        val user =  userRepository.findUserByUserNickname(nickname)?: throw UserNotFoundException()
        return user
    }
    @Transactional(readOnly = false)
    fun deleteUser(authentication: Authentication) {
        val user = authenticationToUser(authentication)
        val spaceUsers = spaceUserRepository.findSpaceUsersByUser(user)
        spaceUsers.forEach {
            it.userNickname = "알 수 없음"
            it.userName = "알 수 없음"
            spaceUserRepository.save(it)
        }
        fcmService.unregisterToken(user)
        userRepository.delete(user)
    }
    @Transactional(readOnly = false)
    fun logoutUser(authentication: Authentication) {
        val user = authenticationToUser(authentication)
        fcmService.unregisterToken(user)

    }
    @Transactional(readOnly = false)
    fun registerToken(authentication: Authentication, token: FCMTokenRequestDto) {
        val user = authenticationToUser(authentication)
        fcmService.registerToken(user, token)
    }

}