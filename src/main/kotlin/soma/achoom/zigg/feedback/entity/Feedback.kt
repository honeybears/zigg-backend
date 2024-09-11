package soma.achoom.zigg.feedback.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.feedback.dto.FeedbackType
import soma.achoom.zigg.space.entity.SpaceUser
import java.util.*

@Entity
class Feedback(
    @Id
    var feedbackId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    var feedbackType: FeedbackType? = FeedbackType.USER,

    var feedbackTimeline: String?,

    var feedbackMessage: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", unique = false)
    var feedbackCreator: SpaceUser,

    @ManyToMany(fetch = FetchType.LAZY)
    var recipients: MutableList<SpaceUser> = mutableListOf(),


    ) : BaseEntity()