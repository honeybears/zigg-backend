package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.repository.HistoryRepository
import soma.achoom.zigg.v0.repository.SpaceRepository

@Service
class HistoryService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val spaceRepository: SpaceRepository,
    private val s3Service: S3Service,
) : BaseService() {
    fun createHistory() {

    }
    fun uploadHistoryToS3(authentication: Authentication, historyId: Long) {
        val space = spaceRepository.findSpaceByHistoriesContains(historyRepository.findById(historyId).get())
            ?: throw IllegalArgumentException("space not found")


    }

}