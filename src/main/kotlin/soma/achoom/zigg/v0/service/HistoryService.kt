package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.repository.HistoryRepository

@Service
class HistoryService @Autowired constructor(
    private val historyRepository: HistoryRepository
) : BaseService() {
    fun createHistory() {

    }

}