package soma.achoom.zigg.firebase.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import soma.achoom.zigg.firebase.entity.FCMToken
import soma.achoom.zigg.user.entity.User

interface FCMRepository : JpaRepository<FCMToken, Long> {
    fun findByToken(token: String): FCMToken?
    fun findFCMTokenByUser(user: User): FCMToken?
}