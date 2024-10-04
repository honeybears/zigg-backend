package soma.achoom.zigg.content.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val imageId: Long? = null,

    @ManyToOne
    var uploader: User?,

    val imageKey: String,

    ) : BaseEntity() {

}