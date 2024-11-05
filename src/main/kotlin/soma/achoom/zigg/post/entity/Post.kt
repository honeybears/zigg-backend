package soma.achoom.zigg.post.entity

import jakarta.persistence.*
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity(name = "post")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val postId: Long? = null,

    @ManyToOne(cascade = [CascadeType.MERGE], fetch = FetchType.LAZY)
    val board: Board,

    @ManyToOne
    @JoinColumn(name = "creator")
    val creator: User,

    @Column(name = "title")
    var title: String,

    @Column(name = "text_content")
    var textContent: String,

    @OneToMany(cascade = [CascadeType.PERSIST])
    var imageContents: MutableList<Image> = mutableListOf(),

    @ManyToOne(cascade = [CascadeType.PERSIST])
    var videoContent: Video? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    var videoThumbnail: Image? = null,

    ) : BaseEntity() {

}