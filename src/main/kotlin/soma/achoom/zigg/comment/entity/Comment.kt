package soma.achoom.zigg.comment.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentId : Long? = null,

    @ManyToOne
    var commentCreator : User,

    var commentMessage : String,

    var commentLike : Long = 0

    ) : BaseEntity() {

}