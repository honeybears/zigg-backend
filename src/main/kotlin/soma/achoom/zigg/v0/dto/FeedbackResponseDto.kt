package soma.achoom.zigg.v0.dto

import jakarta.persistence.*
import org.jetbrains.annotations.Nullable
import soma.achoom.zigg.v0.model.Feedback
import soma.achoom.zigg.v0.model.FeedbackType
import kotlin.time.Duration

data class FeedbackResponseDto(
    val id:Long?,
    @Enumerated(EnumType.STRING)
    val type: FeedbackType?,
    val timeline: Duration?,
    @Nullable
    val creatorId: UserResponseDto?,
    val recipientId:MutableSet<UserResponseDto>?,
    ) : BaseResponseDto(){
        companion object {

            fun from(feedback: Feedback): FeedbackResponseDto {
                return FeedbackResponseDto(
                    id = feedback.id,
                    type = feedback.type,
                    timeline = feedback.timeline,
                    creatorId = UserResponseDto.from(feedback.creatorId),
                    recipientId = feedback.recipientId.mapNotNull { recipientId ->
                        UserResponseDto.from(recipientId) // UserResponseDto 변환
                    }.toMutableSet()
                )
            }
        }


}