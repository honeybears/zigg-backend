package soma.achoom.zigg.v0.history.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.v0.feedback.dto.FeedbackResponseDto
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HistoryResponseDto(
    val historyId: UUID?,
    val historyName: String?,
    val historyVideoPreSignedUrl:String,
    val feedbacks: MutableSet<FeedbackResponseDto>?,
    val historyVideoThumbnailPreSignedUrl:String
)
