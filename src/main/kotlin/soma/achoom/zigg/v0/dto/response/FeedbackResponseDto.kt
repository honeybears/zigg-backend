package soma.achoom.zigg.v0.dto.response

import jakarta.persistence.*
import org.jetbrains.annotations.Nullable
import soma.achoom.zigg.v0.dto.BaseResponseDto
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



}