package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import soma.achoom.zigg.v0.auth.OAuthProviderEnum
import soma.achoom.zigg.v0.model.User
import java.util.UUID


interface UserRepository : JpaRepository<User, UUID> {
    fun existsUserByUserNickname(nickname:String): Boolean
    fun findUserByUserNickname(nickname:String): User?
    fun existsUserByUserNicknameNotNullAndUserNickname(nickname:String): Boolean
    fun findUserByPlatformAndProviderId(provider:OAuthProviderEnum, providerId:String): User?
    fun findUserByUserId(userId:UUID): User?
    fun findUsersByUserNicknameLike(nickname:String): List<User>
}