package soma.achoom.zigg.v0.dto.request

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.jetbrains.annotations.Nullable
import soma.achoom.zigg.v0.model.FeedbackType
import kotlin.time.Duration

data class FeedbackRequestDto(
    val id:Long?,
    @Enumerated(EnumType.STRING)
    val type: FeedbackType?,
    val timeline: Duration?,
    @Nullable
    val creatorId: UserRequestDto?,
    val recipientId:MutableSet<UserRequestDto>?,
) {

}