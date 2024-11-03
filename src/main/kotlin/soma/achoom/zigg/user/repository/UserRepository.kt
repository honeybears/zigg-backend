package soma.achoom.zigg.user.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.user.entity.User
import java.util.UUID


interface UserRepository : JpaRepository<User, Long> {
    fun existsUserByNickname(nickname:String): Boolean
    fun findUserByNickname(nickname:String): User?
    fun findUserByPlatformAndProviderId(provider: OAuthProviderEnum, providerId:String): User?
    fun findUserByUserId(userId:Long): User?
    @Query("SELECT u FROM User u WHERE u.name LIKE :userName%")
    fun findUsersByUserNameLike(userName:String, pageable: Pageable): List<User>
}