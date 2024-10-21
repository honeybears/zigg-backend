package soma.achoom.zigg.post.entity

import jakarta.persistence.*
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User
@EntityListeners(PostEntityListener::class)
@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val postId: Long? = null,

    @ManyToOne
    val board: Board,

    @ManyToOne
    val creator: User,

    var likes: Long = 0,

    var title: String,

    var textContent: String,

    @OneToMany
    var imageContents: MutableSet<Image> = mutableSetOf(),

    @ManyToOne
    var videoContent: Video? = null,
    @ManyToOne
    var videoThumbnail: Image? = null ,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var comments: MutableSet<Comment> = mutableSetOf()

) : BaseEntity() {
    fun detach(){
        board.posts.remove(this)
    }
}