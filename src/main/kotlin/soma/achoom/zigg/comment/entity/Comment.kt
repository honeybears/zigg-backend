package soma.achoom.zigg.comment.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentId : Long? = null,

    @ManyToOne
    @JoinColumn(name = "creator")
    var creator: User,
    @Column(name = "text_comment")
    var textComment: String,
    @Column(name = "likes")
    var likes : Int = 0,

    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val replies: MutableList<Comment> = mutableListOf(),
    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
    ) : BaseEntity() {

}