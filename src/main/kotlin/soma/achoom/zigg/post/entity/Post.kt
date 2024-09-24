package soma.achoom.zigg.post.entity

import jakarta.persistence.*
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val postId: Long? = null,

    @ManyToOne
    val postCreator: User,

    var postLike: Long = 0,

    var postTitle: String,

    var postMessage: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var postImageContent: MutableSet<Image> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var postVideoContent: MutableSet<Video> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val postComments: MutableSet<Comment> = mutableSetOf()

) : BaseEntity() {
}