package soma.achoom.zigg.v0.dto.response

import soma.achoom.zigg.v0.dto.BaseResponseDto

data class HistoryResponseDto(
    val id:Long?,
    val feedbacks: MutableSet<FeedbackResponseDto>?,
) : BaseResponseDto()