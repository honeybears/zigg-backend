package soma.achoom.zigg.v0.auth

import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    private val delegate : OAuth2User,
    val registrationId:String
) :OAuth2User by delegate