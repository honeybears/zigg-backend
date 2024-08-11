package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import java.util.UUID

interface HistoryRepository : JpaRepository<History,Long>{
    fun findHistoryByHistoryId(historyId: UUID): History?
    fun findHistoriesBySpace(space: Space): List<History>
}