package soma.achoom.zigg.v0.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.dto.request.UserRequestDto
import soma.achoom.zigg.v0.dto.response.UserResponseDto
import soma.achoom.zigg.v0.exception.UserAlreadyExistsException
import soma.achoom.zigg.v0.exception.UserNotFoundException

@Service
class UserService: BaseService() {
    fun searchUser(authentication: Authentication, nickname: String): UserResponseDto {
        val user = userRepository.findUserByUserNickname(nickname) ?: throw UserNotFoundException()
        return UserResponseDto.from(user)
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