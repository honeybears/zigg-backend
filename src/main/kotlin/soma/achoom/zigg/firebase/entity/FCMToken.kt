package soma.achoom.zigg.firebase.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
class FCMToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val fcmId: Long? = null,
    var token: String,

) : BaseEntity()