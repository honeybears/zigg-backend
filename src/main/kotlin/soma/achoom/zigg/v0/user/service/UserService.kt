package soma.achoom.zigg.v0.user.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.user.exception.UserAlreadyExistsException
import soma.achoom.zigg.v0.user.exception.UserNotFoundException
import soma.achoom.zigg.global.util.BaseService
import soma.achoom.zigg.v0.user.dto.UserRequestDto
import soma.achoom.zigg.v0.user.dto.UserResponseDto

@Service
class UserService: BaseService() {

    fun searchUser(authentication: Authentication, nickname: String): MutableSet<UserResponseDto> {
        val users = userRepository.findUsersByUserNicknameLike(nickname) ?: throw UserNotFoundException()
        return users.map(UserResponseDto::from).toMutableSet()
    }

    fun getUserInfo(authentication: Authentication): UserResponseDto {
        val user = getAuthUser(authentication)
        return UserResponseDto.from(user)
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
        return UserResponseDto.from(user)
    }

}