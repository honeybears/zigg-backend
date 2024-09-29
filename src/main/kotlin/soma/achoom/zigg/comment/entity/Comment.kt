package soma.achoom.zigg.comment.entity

import jakarta.persistence.*
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

    var commentLike : Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    var parent : Comment?,

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    val commentsReplies: MutableList<Comment> = mutableListOf()

    ) : BaseEntity() {

}