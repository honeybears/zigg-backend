package soma.achoom.zigg.v0.dto.request

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import soma.achoom.zigg.v0.model.Feedback
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.SpaceUser

data class FeedbackRequestDto(
    @Enumerated(EnumType.STRING)
    val feedbackTimeline: String?,
    val feedbackMessage : String?,
    val recipientId:MutableSet<Long>,
) {
    fun toFeedBack(creator: SpaceUser, recipients: MutableSet<SpaceUser>,history: History): Feedback {
        return Feedback(
            feedbackId = null,
            feedbackTimeline = feedbackTimeline!!,
            feedbackMessage = feedbackMessage,
            feedbackCreator = creator,
            recipients = recipients,
            history = history
        )

    }
}