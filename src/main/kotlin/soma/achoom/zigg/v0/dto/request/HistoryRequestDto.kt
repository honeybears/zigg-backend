package soma.achoom.zigg.v0.dto.request

import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space

data class HistoryRequestDto(
    val id:Long?,
    val historyName: String?
){
    fun toHistory(space: Space): History{
        return History(
            historyId = null,
            historyName = historyName,
            space = space,
        )
    }
}