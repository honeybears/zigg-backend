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
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.dto.UserRequestDto
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.entity.UserRole
import soma.achoom.zigg.user.exception.GuestUserUpdateProfileLimitationException
import soma.achoom.zigg.user.exception.UserNotFoundException
import soma.achoom.zigg.user.repository.UserRepository


@Service
class UserService(
    private val userRepository: UserRepository,
    private val responseDtoManager: ResponseDtoManager,
    private val fcmService: FCMService,
    private val spaceUserRepository: SpaceUserRepository,
) {
    @Transactional(readOnly = true)
    fun searchUser(authentication: Authentication, nickname: String): List<User> {
        val users = userRepository.findUsersByUserNameLike(nickname,PageRequest.of(0,10))
        return users.filter { it.userNickname != authenticationToUser(authentication).userNickname }
    }
    @Transactional(readOnly = true)
    fun getUserInfo(authentication: Authentication): User {
        val user = authenticationToUser(authentication)
        return user
    }
    @Transactional(readOnly = false)
    fun updateUser(authentication: Authentication, userRequestDto: UserRequestDto): User {
        val user = authenticationToUser(authentication)
        checkGuestUserUpdateProfileLimit(user)
        user.userName = userRequestDto.userName
//        user.userNickname = userRequestDto.userNickname
        user.profileImageKey = userRequestDto.profileImageUrl?.let {
            it.split("?")[0].split("/").subList(3, userRequestDto.profileImageUrl.split("?")[0].split("/").size)
                .joinToString("/")
                } ?: user.profileImageKey

        user.profileBannerImageKey = userRequestDto.profileBannerImageUrl?.let {
            it.split("?")[0].split("/").subList(3, userRequestDto.profileBannerImageUrl.split("?")[0].split("/").size)
                .joinToString("/")
        } ?: user.profileBannerImageKey

        return userRepository.save(user)
    }
    @Transactional(readOnly = true)
    fun authenticationToUser(authentication: Authentication): User {
        val providerId = authentication.name
        val userDetails = authentication.principal as CustomUserDetails
        val user = userRepository.findUserByPlatformAndProviderId(
            OAuthProviderEnum.valueOf(userDetails.getOAuthProvider()), providerId) ?: throw IllegalArgumentException("user not found")
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
        user.spaces.forEach {
            spaceUser -> spaceUser.user = null
            spaceUserRepository.save(spaceUser)
        }
        userRepository.delete(user)
    }
    @Transactional(readOnly = false)
    fun logoutUser(authentication: Authentication, token: FCMTokenRequestDto) {
        val user = authenticationToUser(authentication)
        fcmService.unregisterToken(user,token)
    }
    @Transactional(readOnly = false)
    fun registerToken(authentication: Authentication, token: FCMTokenRequestDto) {
        val user = authenticationToUser(authentication)
        fcmService.registerToken(user,token)
    }

    private fun checkGuestUserUpdateProfileLimit(user: User) {
        if (user.role == UserRole.GUEST){
            throw GuestUserUpdateProfileLimitationException()
        }
    }

}