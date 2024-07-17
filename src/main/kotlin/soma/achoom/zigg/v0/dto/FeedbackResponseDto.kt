package soma.achoom.zigg.v0.dto

import jakarta.persistence.*
import org.jetbrains.annotations.Nullable
import soma.achoom.zigg.v0.model.Feedback
import soma.achoom.zigg.v0.model.FeedbackType
import soma.achoom.zigg.v0.model.User
import kotlin.time.Duration

data class FeedbackResponseDto(
    val id:Long?,
    @Enumerated(EnumType.STRING)
    val type: FeedbackType?,
    val timeline: Duration?,
    @Nullable
    val creatorId: User?,
    val recipientId:MutableSet<User>?,
    ) : BaseResponseDto(){
        companion object {
            fun from(feedback: Feedback): FeedbackResponseDto {
                return FeedbackResponseDto(
                    id = feedback.id,
                    type = feedback.type,
                    timeline = feedback.timeline,
                    creatorId = feedback.creatorId,
                    recipientId = feedback.recipientId
                )
            }
        }


}