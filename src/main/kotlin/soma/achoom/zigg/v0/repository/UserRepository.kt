package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import soma.achoom.zigg.v0.auth.CustomOAuthProviderEnum
import soma.achoom.zigg.v0.model.User
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByProviderIdAndProvider(id:String,provider:CustomOAuthProviderEnum): Optional<User>
    fun existsUserByUserNickname(nickname:String): Boolean
}