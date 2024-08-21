package soma.achoom.zigg.history.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.space.entity.Space

import java.util.UUID

interface HistoryRepository : JpaRepository<History, Long> {
    fun findHistoryByHistoryId(historyId: UUID): History?
    fun findHistoriesBySpace(space: soma.achoom.zigg.space.entity.Space): List<History>
}