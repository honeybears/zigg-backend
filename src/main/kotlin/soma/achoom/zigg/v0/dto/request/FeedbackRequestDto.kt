package soma.achoom.zigg.v0.dto.request

import org.jetbrains.annotations.NotNull
import soma.achoom.zigg.v0.model.Feedback
import soma.achoom.zigg.v0.model.FeedbackRecipient
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.SpaceUser

data class FeedbackRequestDto(
    val feedbackTimeline: String?,
    val feedbackMessage: String?,
    @field:NotNull
    val recipientId: MutableSet<Long> = mutableSetOf(),
) {
    fun toFeedBack(history: History, creator: SpaceUser, recipients: MutableSet<SpaceUser>): Feedback {
        val feedback = Feedback(
            feedbackId = null,
            feedbackTimeline = feedbackTimeline!!,
            feedbackMessage = feedbackMessage,
            feedbackCreator = creator,
            recipients = mutableSetOf(),
            history = history
        )

        val feedbackRecipient = recipients.map {
            FeedbackRecipient(
                id = null,
                feedback = feedback,
                recipient = it
            )
        }.toMutableSet()
        feedback.recipients.addAll(feedbackRecipient)

        return feedback

    }
}