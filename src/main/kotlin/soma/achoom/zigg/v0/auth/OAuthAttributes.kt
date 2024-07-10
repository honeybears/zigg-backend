package soma.achoom.zigg.v0.auth

import soma.achoom.zigg.v0.model.User
import java.util.*

class OAuthAttributes(
    val attributes: Map<String, Any>,
    val nameAttributeKey: String,
    val name: String,
    val email: String,
) {
    lateinit var registrationId: String
    lateinit var providerId: String
    fun toUser(): User {
        return User(userName = name, email = email, role = UserRole.USER, provider = CustomOAuthProviderEnum.valueOf(
            registrationId.uppercase(Locale.getDefault())), providerId =  providerId)
    }

    companion object {
        fun of(registrationId: String, userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return when (registrationId) {
                "google" -> ofGoogle(userNameAttributeName, attributes)

                else -> ofKakao(userNameAttributeName, attributes)
            }
                TODO("APPLE 공급자 처리")
        }

        private fun ofGoogle(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                name = attributes["name"] as String,
                email = attributes["email"] as String,
                attributes = attributes,
                nameAttributeKey = userNameAttributeName
            )
        }
        private fun ofKakao(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            val properties = attributes["kakao_account"] as Map<String, Any?>
            val nickname = properties["profile"]?.let { (it as Map<String, Any?>)["nickname"] } ?: "Unknown"
            val email = properties["email"] as String



            println("LOG : Generate User by Info")
            println(properties)
            println(nickname)
            println(email)
            println(userNameAttributeName)
            attributes.forEach { (key, value) -> println("$key -> $value") }
            return OAuthAttributes(
                name = nickname as String,
                email = email,
                attributes = attributes,
                nameAttributeKey = userNameAttributeName,
            )
        }

    }
}
