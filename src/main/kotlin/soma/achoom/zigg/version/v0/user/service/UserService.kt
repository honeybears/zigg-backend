package soma.achoom.zigg.version.v0.user.service


import com.google.cloud.storage.Storage
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.global.infra.gcs.GCSService
import soma.achoom.zigg.global.util.BaseService
import soma.achoom.zigg.version.v0.user.dto.UserRequestDto
import soma.achoom.zigg.version.v0.user.dto.UserResponseDto
import soma.achoom.zigg.version.v0.user.exception.UserAlreadyExistsException
import soma.achoom.zigg.version.v0.user.exception.UserNotFoundException


@Service
class UserService(
    private val gcsService: GCSService,
): BaseService() {

    fun searchUser(authentication: Authentication, nickname: String): MutableSet<UserResponseDto> {
        val users = userRepository.findUsersByUserNicknameLike(nickname) ?: throw UserNotFoundException()
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
        val user = getAuthUser(authentication)
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
        val user = getAuthUser(authentication)
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

}