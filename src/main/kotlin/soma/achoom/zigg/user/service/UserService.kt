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
import soma.achoom.zigg.s3.service.S3Service
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
    private val fcmService: FCMService,
    private val spaceUserRepository: SpaceUserRepository,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository,
    private val s3Service: S3Service
) {
    @Transactional(readOnly = true)
    fun searchUser(authentication: Authentication, nickname: String): List<UserResponseDto> {
        val users = userRepository.findUsersByUserNameLike(nickname,PageRequest.of(0,10))
        return users.filter { it.nickname != authenticationToUser(authentication).nickname }.map {
            UserResponseDto(
                userId = it.userId,
                userName = it.name,
                userNickname = it.nickname,
                profileImageUrl = s3Service.getPreSignedGetUrl(it.profileImageKey.imageKey),
                profileBannerImageUrl = s3Service.getPreSignedGetUrl(it.profileBannerImageKey?.imageKey),
                userTags = it.tags,
                userDescription = it.description,
                createdAt = it.createAt
            )
        }.toList()
    }
    @Transactional(readOnly = true)
    fun getUserInfo(authentication: Authentication): UserResponseDto {
        val user = authenticationToUser(authentication)
        return UserResponseDto(
            userId = user.userId,
            userName = user.name,
            userNickname = user.nickname,
            profileImageUrl = s3Service.getPreSignedGetUrl(user.profileImageKey.imageKey),
            profileBannerImageUrl = s3Service.getPreSignedGetUrl(user.profileBannerImageKey?.imageKey),
            userTags = user.tags,
            userDescription = user.description,
            createdAt = user.createAt
        )
    }
    @Transactional(readOnly = true)
    fun getUserInfoByUserId(authentication: Authentication,userRequestDto: UserRequestDto): UserResponseDto {
        authenticationToUser(authentication)
        if (userRequestDto.userId == null) {
            throw IllegalArgumentException("userId is null")
        }
        val user = userRepository.findUserByUserId(userRequestDto.userId)?: throw UserNotFoundException()
        return UserResponseDto(
            userId = user.userId,
            userName = user.name,
            userNickname = user.nickname,
            profileImageUrl = s3Service.getPreSignedGetUrl(user.profileImageKey.imageKey),
            profileBannerImageUrl = s3Service.getPreSignedGetUrl(user.profileBannerImageKey?.imageKey),
            userTags = user.tags,
            userDescription = user.description,
            createdAt = user.createAt
        )
    }

    @Transactional(readOnly = false)
    fun updateUser(authentication: Authentication, userRequestDto: UserRequestDto): UserResponseDto {
        val user = authenticationToUser(authentication)
        checkGuestUserUpdateProfileLimit(user)
        user.name = userRequestDto.userName
        user.description = userRequestDto.userDescription
        user.tags = userRequestDto.userTags

        userRequestDto.profileImageUrl?.let{
            user.profileImageKey = Image.fromUrl(
                imageUrl = it,
                uploader = user
            )
        }

        userRequestDto.profileBannerImageUrl?.let {
            user.profileBannerImageKey = Image.fromUrl(
                imageUrl = it,
                uploader = user
            )
        }
        userRepository.save(user)
        return UserResponseDto(
            userId = user.userId,
            userName = user.name,
            userNickname = user.nickname,
            profileImageUrl = s3Service.getPreSignedGetUrl(user.profileImageKey.imageKey),
            profileBannerImageUrl = s3Service.getPreSignedGetUrl(user.profileBannerImageKey?.imageKey),
            userTags = user.tags,
            userDescription = user.description,
            createdAt = user.createAt
        )
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
        spaceUserRepository.findSpaceUsersByUser(user).forEach {
            it.user = null
            spaceUserRepository.save(it)
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