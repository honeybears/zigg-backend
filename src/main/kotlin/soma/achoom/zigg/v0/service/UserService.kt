package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.auth.CustomOAuth2User
import soma.achoom.zigg.v0.auth.CustomOAuthProviderEnum
import soma.achoom.zigg.v0.dto.UserRequestDto
import soma.achoom.zigg.v0.dto.UserResponseDto
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.repository.UserRepository
import java.util.*

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun userNickNameIsValid(nickname: String): Boolean {
        return userRepository.existsUserByUserNickname(nickname)
    }

    fun updateUserNickName(auth: CustomOAuth2User,userRequestDto: UserRequestDto): UserResponseDto {
        val user = getAuthUser(auth)
        user.userNickname = userRequestDto.userNickname
        userRepository.save(user)
        return UserResponseDto.from(user)
    }



    private fun getAuthUser(oAuth2User: CustomOAuth2User):User{
        return userRepository.findByProviderIdAndProvider(
            oAuth2User.name,
            CustomOAuthProviderEnum.valueOf(oAuth2User.registrationId.uppercase(Locale.getDefault()))
        ).get()
    }

}