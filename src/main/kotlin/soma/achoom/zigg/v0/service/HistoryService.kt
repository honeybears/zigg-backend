package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.dto.request.HistoryRequestDto
import soma.achoom.zigg.v0.dto.response.HistoryResponseDto
import soma.achoom.zigg.v0.exception.SpaceNotFoundException
import soma.achoom.zigg.v0.repository.HistoryRepository
import soma.achoom.zigg.v0.repository.SpaceRepository

@Service
class HistoryService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val spaceRepository: SpaceRepository,
) : BaseService() {

    fun createHistory(authentication: Authentication, spaceId: Long, historyRequestDto: HistoryRequestDto) : HistoryResponseDto{
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRequestDto.toHistory(space)
        historyRepository.save(history)
        return HistoryResponseDto.from(history)
    }

    fun getHistories(authentication: Authentication, spaceId: Long): List<HistoryResponseDto> {
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val histories = historyRepository.findHistoriesBySpace(space)
        return histories.filter{!it.isDeleted}.map { HistoryResponseDto.from(it) }
    }

    fun getHistory(authentication: Authentication, spaceId: Long, historyId: Long): HistoryResponseDto {
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        return HistoryResponseDto.from(history)
    }
    fun updateHistory(authentication: Authentication, spaceId: Long, historyId: Long, historyRequestDto: HistoryRequestDto): HistoryResponseDto {
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        history.isDeleted = true
        historyRepository.save(history)
        return HistoryResponseDto.from(history)
    }
    fun deleteHistory(authentication: Authentication, spaceId: Long, historyId: Long) {
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        history.isDeleted = true
        historyRepository.save(history)
    }

}