package soma.achoom.zigg.history.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HistoryResponseDto(
    val historyId: UUID?,
    val historyName: String?,
    val historyVideoPreSignedUrl:String,
    val feedbacks: MutableSet<FeedbackResponseDto>?,
    val historyVideoThumbnailPreSignedUrl:String,
    val createdAt:LocalDateTime?,
    val videoDuration: String?
)