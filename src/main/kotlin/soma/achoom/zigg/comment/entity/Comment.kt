package soma.achoom.zigg.comment.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentId : Long? = null,

    @ManyToOne
    var creator: User,

    var textComment: String,

    var likes: Long = 0,

    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val replies: MutableList<Comment> = mutableListOf(),

    ) : BaseEntity() {

}