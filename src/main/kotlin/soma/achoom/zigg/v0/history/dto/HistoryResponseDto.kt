package soma.achoom.zigg.v0.history.dto

import soma.achoom.zigg.v0.feedback.dto.FeedbackResponseDto
import java.util.UUID

data class HistoryResponseDto(
    val historyId: UUID?,
    val historyName: String?,
    val historyVideoPreSignedUrl:String,
    val feedbacks: MutableSet<FeedbackResponseDto>?,
)
