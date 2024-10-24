package soma.achoom.zigg.post.entity

import com.fasterxml.jackson.annotation.JsonIgnore
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

    @ManyToOne(cascade = [CascadeType.MERGE], fetch = FetchType.LAZY)
    val board: Board,

    @ManyToOne
    val creator: User,

    var title: String,

    var textContent: String,

    @OneToMany
    var imageContents: MutableSet<Image> = mutableSetOf(),

    @ManyToOne
    var videoContent: Video? = null,
    @ManyToOne
    var videoThumbnail: Image? = null ,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val comments: MutableSet<Comment> = mutableSetOf(),

    var likeCnt: Int = 0,

    var scrapCnt: Int = 0,

    @OneToMany(cascade = [CascadeType.REMOVE])
    val likes: MutableSet<PostLike> = mutableSetOf(),
    @OneToMany(cascade = [CascadeType.REMOVE])
    val scraps: MutableSet<PostScrap> = mutableSetOf()

) : BaseEntity() {
    fun detach(){
        board.posts.remove(this)
    }
}