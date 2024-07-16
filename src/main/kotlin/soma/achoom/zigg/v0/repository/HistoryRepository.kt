package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.History

interface HistoryRepository : JpaRepository<History,Long>{
}