package soma.achoom.zigg.v0.history.dto

import soma.achoom.zigg.v0.history.entity.History
import soma.achoom.zigg.v0.space.entity.Space
import java.util.UUID

data class HistoryRequestDto(
    val historyName: String?,
    val historyVideoUrl: String?
){

}