package soma.achoom.zigg.firebase.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity(name = "fcm_token")
class FCMToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val fcmId: Long? = null,
    @Column(name = "token")
    var token: String,

) : BaseEntity()