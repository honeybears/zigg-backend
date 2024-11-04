package soma.achoom.zigg.history.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HistoryResponseDto(
    val historyId: Long?,
    val historyName: String?,
    val historyVideoPreSignedUrl:String,
    val feedbacks: MutableSet<FeedbackResponseDto>? = mutableSetOf(),
    val historyVideoThumbnailPreSignedUrl:String,
    val createdAt:LocalDateTime?,
    val videoDuration: String?,
    val feedbackCount : Int?
){
    override fun toString(): String {
        return "HistoryResponseDto(\n" +
                "historyId=$historyId,\n" +
                "historyName=$historyName,\n" +
                "historyVideoPreSignedUrl=$historyVideoPreSignedUrl,\n" +
                "feedbacks=${feedbacks?.map { it.toString() }},\n" +
                "historyVideoThumbnailPreSignedUrl=$historyVideoThumbnailPreSignedUrl,\n" +
                "createdAt=$createdAt,\n" +
                "videoDuration=$videoDuration,\n" +
                "feedbackCount=$feedbackCount\n" +
                ")"
    }
}
