package soma.achoom.zigg.global.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.auth.filter.CustomUserDetails
import soma.achoom.zigg.v0.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.v0.user.entity.User
import soma.achoom.zigg.v0.user.repository.UserRepository
@Service
abstract class BaseService{
    @Autowired
    protected lateinit var userRepository: UserRepository
    protected fun getAuthUser(authentication: Authentication): User {
        val providerId = authentication.name
        val userDetails = authentication.principal as CustomUserDetails

        val user = userRepository.findUserByPlatformAndProviderId(
            OAuthProviderEnum.valueOf(userDetails.getOAuthProvider()), providerId
        ) ?: throw IllegalArgumentException("user not found")
        return user
    }
}