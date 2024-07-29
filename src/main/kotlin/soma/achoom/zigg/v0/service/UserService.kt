package soma.achoom.zigg.v0.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.dto.request.UserRequestDto
import soma.achoom.zigg.v0.dto.response.UserResponseDto

@Service
class UserService: BaseService() {
    fun getUserInfo(authentication: Authentication): UserResponseDto {
        val user = getAuthUser(authentication)
        return UserResponseDto.from(user)
    }


    fun userNickNameIsValid(nickName: String): Boolean {
        return userRepository.existsUserByUserNicknameNotNullAndUserNickname(nickName)
    }

    fun updateUserNickName(authentication: Authentication,userRequestDto: UserRequestDto): UserResponseDto {
        val user = getAuthUser(authentication)
        user.userNickname = userRequestDto.userNickname + "#" + user.userId
        userRepository.save(user)
        return UserResponseDto.from(user)
    }


    fun updateUser(authentication: Authentication, userRequestDto: UserRequestDto): UserResponseDto {
        val user = getAuthUser(authentication)
        user.userName = userRequestDto.userName
        user.userNickname = userRequestDto.userNickname + "#" + user.userId
        userRepository.save(user)
        return UserResponseDto.from(user)
    }

}