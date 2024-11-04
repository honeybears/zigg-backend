package soma.achoom.zigg.comment.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
@EntityListeners(CommentLikeEntityListener::class)
class CommentLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentLikeId: Long? = null,

    @ManyToOne
    var comment: Comment?,

    @ManyToOne
    var user: User?
) : BaseEntity() {


}