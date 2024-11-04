package soma.achoom.zigg.feedback.dto

import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull

import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.space.entity.SpaceUser
import java.util.UUID

data class FeedbackRequestDto(
    val feedbackTimeline: String?,
    val feedbackMessage: String?,
    @NotNull
    @Min(value = 1, message = "받는 사람은 최소 1명 이상이어야 합니다.")
    val recipientId: MutableSet<Long> = mutableSetOf(),
) {
    fun toFeedBack(history: History, creator: SpaceUser, recipients: MutableSet<SpaceUser>): Feedback {
        val feedback = Feedback(
            timeline = feedbackTimeline!!,
            message = feedbackMessage,
            creator = creator,
            recipients = mutableListOf(),
//            history = history
        )


        feedback.recipients.addAll(recipients)

        return feedback

    }
}