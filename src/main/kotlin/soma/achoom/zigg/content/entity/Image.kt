package soma.achoom.zigg.content.entity

import jakarta.persistence.*
import org.jetbrains.annotations.TestOnly
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity(name = "image")
class Image private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val imageId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader")
    var uploader: User?,
    @Column(name = "image_key")
    val imageKey: String,

    ) : BaseEntity() {

    @TestOnly
    constructor(uploader: User?, imageKey: String) : this(
        imageId = null,
        uploader = uploader,
        imageKey = imageKey
    )

    companion object{
        fun fromUrl(imageUrl: String, uploader: User): Image {
            val imageKey = imageUrl.split("?")[0]
                .split("/")
                .subList(3, imageUrl.split("?")[0].split("/").size)
                .joinToString("/")
            return Image(uploader = uploader, imageKey = imageKey)
        }
    }
}