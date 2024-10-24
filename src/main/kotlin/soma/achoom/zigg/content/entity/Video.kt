package soma.achoom.zigg.content.entity

import jakarta.persistence.*
import org.jetbrains.annotations.TestOnly
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
class Video private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val videoId: Long? = null,

    @ManyToOne
    var uploader: User?,

    val videoKey: String,

    val duration: String,

    ) : BaseEntity() {

    @TestOnly
    constructor(uploader: User?, videoKey: String, duration: String) : this(
        videoId = null,
        uploader = uploader,
        videoKey = videoKey,
        duration = duration
    )
    companion object {
        fun fromUrl(videoUrl: String, uploader: User, duration: String): Video {
            val videoKey = videoUrl.split("?")[0]
                .split("/")
                .subList(3, videoUrl.split("?")[0].split("/").size)
                .joinToString("/")
            return Video(uploader = uploader, videoKey = videoKey, duration = duration)
        }
    }
}