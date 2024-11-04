package soma.achoom.zigg.space.entity

import jakarta.persistence.*
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.global.BaseEntity

import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.invite.entity.Invite
import java.time.LocalDateTime

@Entity(name = "space")
class Space(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var spaceId: Long? = null,

    var name: String,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    var imageKey: Image,
    @Column(name = "reference_video_key")
    var referenceVideoKey: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var histories: MutableSet<History> = mutableSetOf(),


    ) : BaseEntity() {

    fun addHistory(history: History) {
        histories.add(history)
        this.updateAt = LocalDateTime.now()
    }

}