package soma.achoom.zigg.user.service
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.auth.filter.CustomUserDetails
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.repository.ImageRepository
import soma.achoom.zigg.content.repository.VideoRepository
import soma.achoom.zigg.firebase.dto.FCMTokenRequestDto
import soma.achoom.zigg.firebase.service.FCMService
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.dto.UserRequestDto
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.entity.UserRole
import soma.achoom.zigg.user.exception.GuestUserUpdateProfileLimitationException
import soma.achoom.zigg.user.exception.UserNotFoundException
import soma.achoom.zigg.user.repository.UserRepository


@Service
class UserService(
    private val userRepository: UserRepository,
    private val fcmService: FCMService,
    private val spaceUserRepository: SpaceUserRepository,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
) {
    @Transactional(readOnly = true)
    fun searchUser(authentication: Authentication, nickname: String): List<User> {
        val users = userRepository.findUsersByUserNameLike(nickname,PageRequest.of(0,10))
        return users.filter { it.nickname != authenticationToUser(authentication).nickname }
    }
    @Transactional(readOnly = true)
    fun getUserInfo(authentication: Authentication): User {
        val user = authenticationToUser(authentication)
        return user
    }
    @Transactional(readOnly = true)
    fun getUserInfoByUserId(authentication: Authentication,userRequestDto: UserRequestDto): User {
        authenticationToUser(authentication)
        if (userRequestDto.userId == null) {
            throw IllegalArgumentException("userId is null")
        }
        val user = userRepository.findUserByUserId(userRequestDto.userId)?: throw UserNotFoundException()
        return user
    }

    @Transactional(readOnly = false)
    fun updateUser(authentication: Authentication, userRequestDto: UserRequestDto): User {
        val user = authenticationToUser(authentication)
        checkGuestUserUpdateProfileLimit(user)
        user.name = userRequestDto.userName
        user.description = userRequestDto.userDescription
        user.tags = userRequestDto.userTags

        userRequestDto.profileImageUrl?.let{

            user.profileImageKey = Image(
                imageKey = it.split("?")[0].split("/").subList(3, userRequestDto.profileImageUrl.split("?")[0].split("/").size)
                    .joinToString("/"),
                uploader = user
            )
            imageRepository.save(user.profileImageKey)
        }

        userRequestDto.profileBannerImageUrl?.let {
            user.profileBannerImageKey = Image(
                imageKey = it.split("?")[0].split("/").subList(3, userRequestDto.profileBannerImageUrl.split("?")[0].split("/").size)
                    .joinToString("/"),
                uploader = user
            )

            imageRepository.save(user.profileBannerImageKey!!)

        }

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
        val user =  userRepository.findUserByNickname(nickname)?: throw UserNotFoundException()
        return user
    }
    @Transactional(readOnly = false)
    fun deleteUser(authentication: Authentication) {
        val user = authenticationToUser(authentication)
        user.spaces.forEach {
            spaceUser -> spaceUser.user = null
            spaceUserRepository.save(spaceUser)
        }
        imageRepository.findImagesByUploader(user).forEach {
            it.uploader = null
            imageRepository.save(it)
        }
        videoRepository.findVideosByUploader(user).forEach {
            it.uploader = null
            videoRepository.save(it)
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