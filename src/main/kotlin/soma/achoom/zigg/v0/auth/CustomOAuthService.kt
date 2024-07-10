package soma.achoom.zigg.v0.auth

import jakarta.servlet.http.HttpSession
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.repository.UserRepository
import java.util.*
import kotlin.jvm.optionals.getOrDefault
import kotlin.jvm.optionals.getOrElse


@Service
class CustomOAuthService(
    private val userRepository: UserRepository,
    private val httpSession: HttpSession
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oauth2User = delegate.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
//        println(1)
//        println(registrationId)
        val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
//        println(2)
//        println(userNameAttributeName)
//        println(3)
//        oauth2User.attributes.forEach { keyValue -> println(keyValue) }

        val attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauth2User.attributes)
        attributes.registrationId = registrationId
        attributes.providerId = oauth2User.attributes[userNameAttributeName].toString()
        val user = saveOrUpdate(attributes)
        httpSession.setAttribute("user", user)

        return CustomOAuth2User(
            DefaultOAuth2User(setOf(SimpleGrantedAuthority(user.role.toString())),
                attributes.attributes,
                attributes.nameAttributeKey)
            ,registrationId
        )
    }


    fun saveOrUpdate(attributes: OAuthAttributes): User {
        val userOpt = userRepository.findByEmail(attributes.email)
        if(userOpt.isEmpty) {
            return userRepository.save(attributes.toUser())
        }
        val user = userOpt.get()
        if(user.provider.name != attributes.providerId) {
            user.providerId = attributes.providerId
            user.provider = CustomOAuthProviderEnum.valueOf(attributes.registrationId.uppercase(Locale.getDefault()))
        }
        return userRepository.save(user)
    }
}
