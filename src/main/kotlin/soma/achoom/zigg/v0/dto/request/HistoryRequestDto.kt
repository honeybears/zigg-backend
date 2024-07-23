package soma.achoom.zigg.v0.dto.request

data class HistoryRequestDto(
    val id:Long?,
    val feedbacks: MutableSet<FeedbackRequestDto>?,
)
