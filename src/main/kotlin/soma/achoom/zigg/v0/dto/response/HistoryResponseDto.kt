package soma.achoom.zigg.v0.dto.response

import soma.achoom.zigg.v0.dto.BaseResponseDto
import soma.achoom.zigg.v0.model.History

data class HistoryResponseDto(
    val id: Long?,
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