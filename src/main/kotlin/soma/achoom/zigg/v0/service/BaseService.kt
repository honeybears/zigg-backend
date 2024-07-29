package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.auth.CustomUserDetails
import soma.achoom.zigg.v0.auth.OAuthProviderEnum
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.repository.UserRepository
@Service
abstract class BaseService{
    @Autowired
    protected lateinit var userRepository: UserRepository
    protected fun getAuthUser(authentication: Authentication): User {
        val providerId = authentication.name
        val userDetails = authentication.principal as CustomUserDetails

        val user = userRepository.findUserByProviderAndProviderId(
            OAuthProviderEnum.valueOf(userDetails.getOAuthProvider()), providerId
        ) ?: throw IllegalArgumentException("user not found")
        println(user.userId)
        println(user.providerId)
        println(user.provider)
        return user
    }
}