package soma.achoom.zigg.content.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
class Video(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val videoId: Long? = null,

    @ManyToOne
    var videoUploader: User?,

    val videoKey: String,

    val videoDuration: String,

    ) : BaseEntity() {


}