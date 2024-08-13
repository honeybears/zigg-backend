package soma.achoom.zigg.v0.feedback.dto

import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull
import soma.achoom.zigg.v0.feedback.entity.Feedback
import soma.achoom.zigg.v0.feedback.entity.FeedbackRecipient
import soma.achoom.zigg.v0.history.entity.History
import soma.achoom.zigg.v0.space.entity.SpaceUser
import java.util.UUID

data class FeedbackRequestDto(
    val feedbackTimeline: String?,
    val feedbackMessage: String?,
    @NotNull
    @Min(value = 1, message = "받는 사람은 최소 1명 이상이어야 합니다.")
    val recipientId: MutableSet<UUID> = mutableSetOf(),
) {
    fun toFeedBack(history: History, creator: SpaceUser, recipients: MutableSet<SpaceUser>): Feedback {
        val feedback = Feedback(
            feedbackTimeline = feedbackTimeline!!,
            feedbackMessage = feedbackMessage,
            feedbackCreator = creator,
            recipients = mutableSetOf(),
            history = history
        )

        val feedbackRecipient = recipients.map {
            FeedbackRecipient(
                feedback = feedback,
                recipient = it
            )
        }.toMutableSet()
        feedback.recipients.addAll(feedbackRecipient)

        return feedback

    }
}