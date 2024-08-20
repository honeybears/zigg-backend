package soma.achoom.zigg.feedback.controller

import jakarta.validation.Valid
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import soma.achoom.zigg.ai.dto.GenerateAIFeedbackResponseDto
import soma.achoom.zigg.feedback.dto.FeedbackRequestDto
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.feedback.service.FeedbackService
import java.util.UUID

@RestController
@RequestMapping("/api/v0/spaces/histories/feedbacks")
class FeedbackController @Autowired constructor(private val feedbackService: FeedbackService) {

    @GetMapping("{spaceId}/{historyId}")
    fun getFeedbacks(authentication: Authentication, @PathVariable spaceId: UUID, @PathVariable historyId: UUID): ResponseEntity<List<FeedbackResponseDto>> {
        val feedbackResponseDto = feedbackService.getFeedbacks(authentication, spaceId, historyId)
        return ResponseEntity.ok().body(feedbackResponseDto)
    }

    @PostMapping("{spaceId}/{historyId}")
    fun createFeedback(authentication: Authentication, @PathVariable spaceId: UUID, @PathVariable historyId: UUID, @Valid @RequestBody feedbackRequestDto: FeedbackRequestDto): ResponseEntity<FeedbackResponseDto> {
        val feedbackResponseDto = feedbackService.createFeedback(authentication, spaceId, historyId, feedbackRequestDto)
        return ResponseEntity.ok().body(feedbackResponseDto)
    }

    @PatchMapping("{spaceId}/{historyId}/{feedbackId}")
    fun updateFeedback(authentication: Authentication, @PathVariable spaceId: UUID, @PathVariable historyId: UUID, @PathVariable feedbackId: UUID, @Valid @RequestBody feedbackRequestDto: FeedbackRequestDto): ResponseEntity<FeedbackResponseDto> {
        val feedbackResponseDto = feedbackService.updateFeedback(authentication, spaceId, historyId, feedbackId, feedbackRequestDto)
        return ResponseEntity.ok().body(feedbackResponseDto)
    }

    @DeleteMapping("{spaceId}/{historyId}/{feedbackId}")
    fun deleteFeedback(authentication: Authentication, @PathVariable spaceId: UUID, @PathVariable historyId: UUID, @PathVariable feedbackId: UUID): ResponseEntity<Any> {
        feedbackService.deleteFeedback(authentication, spaceId, historyId, feedbackId)
        return ResponseEntity.noContent().build()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @GetMapping("ai/{spaceId}/{historyId}")
    suspend fun generateAIFeedback(authentication: Authentication, @PathVariable spaceId: UUID, @PathVariable historyId: UUID): ResponseEntity<Void> {
        GlobalScope.launch {
            val feedbackResponseDto = feedbackService.generateAIFeedbackRequest(authentication, spaceId, historyId)

        }
        return ResponseEntity.noContent().build()
    }
    @OptIn(DelicateCoroutinesApi::class)
    @PostMapping("ai/{historyId}")
    suspend fun generatedAiFeedbackFromFastApi(authentication: Authentication, @PathVariable spaceId: UUID, @PathVariable historyId: UUID, @RequestBody generateAIFeedbackResponseDto: GenerateAIFeedbackResponseDto): ResponseEntity<Void> {
        GlobalScope.launch {
            val feedbackResponseDto = feedbackService.generateAIFeedbackResponse(historyId,generateAIFeedbackResponseDto)

        }
        return ResponseEntity.noContent().build()
    }
}