package soma.achoom.zigg.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.user.entity.User
import java.util.UUID


interface UserRepository : JpaRepository<User, UUID> {
    fun existsUserByUserNickname(nickname:String): Boolean
    fun findUserByUserNickname(nickname:String): User?
    fun existsUserByUserNicknameNotNullAndUserNickname(nickname:String): Boolean
    fun findUserByPlatformAndProviderId(provider: OAuthProviderEnum, providerId:String): User?
    fun findUserByUserId(userId:UUID): User?
    @Query("SELECT u FROM User u WHERE u.userNickname LIKE :nickname%")
    fun findUsersByUserNicknameLike(nickname:String): List<User>
}