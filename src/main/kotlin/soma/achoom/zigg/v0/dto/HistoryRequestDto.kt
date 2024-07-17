package soma.achoom.zigg.v0.dto

import soma.achoom.zigg.v0.model.Feedback

data class HistoryRequestDto(
    val id:Long?,

    val feedbacks: MutableSet<Feedback>?,
)
