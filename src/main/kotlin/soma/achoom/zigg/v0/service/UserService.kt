package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.auth.CustomUserDetails
import soma.achoom.zigg.v0.auth.OAuthProviderEnum
import soma.achoom.zigg.v0.dto.request.UserRequestDto
import soma.achoom.zigg.v0.dto.response.UserResponseDto
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.repository.UserRepository

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun userNickNameIsValid(nickName: String): Boolean {
        return userRepository.existsUserByUserNicknameNotNullAndUserNickname(nickName)
    }

    fun updateUserNickName(authentication: Authentication,userRequestDto: UserRequestDto): UserResponseDto {
        val user = getAuthUser(authentication)
        user.userNickname = userRequestDto.userNickname + "#" + user.userId
        userRepository.save(user)
        return UserResponseDto.from(user)
    }



    private fun getAuthUser(authentication: Authentication):User{
        //TODO: authentication을 통해 user 정보를 가져오는 로직
        val providerId = authentication.name
        val userDetails = authentication.principal as CustomUserDetails

        val user = userRepository.findUserByProviderAndProviderId(
            OAuthProviderEnum.valueOf(userDetails.getOAuthProvider()), providerId).get()
        println(user.userId)
        println(user.providerId)
        println(user.provider)
        return user
    }

    fun register(authentication: Authentication, userRequestDto: UserRequestDto): UserResponseDto {
        val user = getAuthUser(authentication)
        user.userName = userRequestDto.userName
        user.userNickname = userRequestDto.userNickname + "#" + user.userId
        userRepository.save(user)
        return UserResponseDto.from(user)
    }

}