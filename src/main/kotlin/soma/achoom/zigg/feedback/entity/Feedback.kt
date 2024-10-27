package soma.achoom.zigg.feedback.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.feedback.dto.FeedbackType
import soma.achoom.zigg.space.entity.SpaceUser
import java.util.*

@Entity
class Feedback(
    @Id
    @Column(name = "feedback_id", columnDefinition = "BINARY(16)")
    var feedbackId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    var type: FeedbackType? = FeedbackType.USER,

    var timeline: String?,

    var message: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", unique = false)
    var creator: SpaceUser,

    @ManyToMany(fetch = FetchType.LAZY)
    var recipients: MutableList<SpaceUser> = mutableListOf(),

    ) : BaseEntity()