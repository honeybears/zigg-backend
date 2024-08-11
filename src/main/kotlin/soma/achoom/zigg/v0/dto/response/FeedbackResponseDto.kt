package soma.achoom.zigg.v0.dto.response

import jakarta.persistence.*
import org.jetbrains.annotations.Nullable
import soma.achoom.zigg.v0.dto.BaseResponseDto
import soma.achoom.zigg.v0.model.Feedback
import soma.achoom.zigg.v0.model.FeedbackRecipient
import soma.achoom.zigg.v0.model.enums.FeedbackType
import soma.achoom.zigg.v0.model.SpaceUser
import java.util.UUID

data class FeedbackResponseDto(
    val feedbackId: UUID?,
    @Enumerated(EnumType.STRING)
    val feedbackType: FeedbackType?,
    val feedbackTimeline: String?,
    val feedbackMessage: String?,
    @Nullable
    val creatorId: SpaceUser?,
    val recipientId: MutableSet<FeedbackRecipient>?,
) : BaseResponseDto() {
    companion object {
        fun from(feedback: Feedback): FeedbackResponseDto {
            return FeedbackResponseDto(
                feedbackId = feedback.feedbackId,
                feedbackType = feedback.feedbackType,
                feedbackTimeline = feedback.feedbackTimeline,
                feedbackMessage = feedback.feedbackMessage,
                creatorId = feedback.feedbackCreator,
                recipientId = feedback.recipients
            )
        }
    }
}