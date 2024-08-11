package soma.achoom.zigg.v0.dto.request

import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import java.util.UUID

data class HistoryRequestDto(
    val id:UUID?,
    val historyName: String?
){
    fun toHistory(space: Space): History{
        return History(
            historyName = historyName,
            space = space,
        )
    }
}