package soma.achoom.zigg.firebase.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
data class FCMToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val fcmId: Long? = null,
    var token: String,
    @OneToOne
    val user: User
) : BaseEntity()