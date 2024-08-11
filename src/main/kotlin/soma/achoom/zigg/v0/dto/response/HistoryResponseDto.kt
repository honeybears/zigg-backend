package soma.achoom.zigg.v0.dto.response

import soma.achoom.zigg.v0.dto.BaseResponseDto
import soma.achoom.zigg.v0.model.History
import java.util.UUID

data class HistoryResponseDto(
    val id: UUID?,
    val historyName: String?,
    val feedbacks: MutableSet<FeedbackResponseDto>?,
) : BaseResponseDto()
{
    companion object {
        fun from(history: History): HistoryResponseDto {
            return HistoryResponseDto(
                id = history.historyId,
                historyName = history.historyName,
                feedbacks = history.feedbacks.map { FeedbackResponseDto.from(it) }.toMutableSet()
            )
        }
    }
}