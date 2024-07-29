package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.auth.OAuthProviderEnum
import soma.achoom.zigg.v0.model.User
import java.util.Optional


interface UserRepository : JpaRepository<User, Long> {
    fun existsUserByUserNickname(nickname:String): Boolean
    fun findUserByUserNickname(nickname:String): User?
    fun existsUserByUserNicknameNotNullAndUserNickname(nickname:String): Boolean
    fun findUserByProviderAndProviderId(provider:OAuthProviderEnum, providerId:String): User?
}