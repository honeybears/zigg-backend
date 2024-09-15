package soma.achoom.zigg.feedback.controller

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import soma.achoom.zigg.feedback.dto.FeedbackRequestDto
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.feedback.service.FeedbackService
import soma.achoom.zigg.global.ResponseDtoManager
import java.util.UUID

@RestController
@RequestMapping("/api/v0/spaces/histories/feedbacks")
class FeedbackController @Autowired constructor(
    private val feedbackService: FeedbackService,
    private val responseDtoManager: ResponseDtoManager
) {

    @GetMapping("{spaceId}/{historyId}")
    fun getFeedbacks(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @PathVariable historyId: UUID
    ): ResponseEntity<List<FeedbackResponseDto>> {
        val feedbacks = feedbackService.getFeedbacks(authentication, spaceId, historyId)
        return ResponseEntity.ok().body(
            feedbacks.map {
                responseDtoManager.generateFeedbackResponseDto(it)
            }
        )
    }

    @PostMapping("{spaceId}/{historyId}")
    fun createFeedback(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @PathVariable historyId: UUID,
        @Valid @RequestBody feedbackRequestDto: FeedbackRequestDto
    ): ResponseEntity<FeedbackResponseDto> {
        val feedback = feedbackService.createFeedback(authentication, spaceId, historyId, feedbackRequestDto)
        return ResponseEntity.ok().body(responseDtoManager.generateFeedbackResponseDto(feedback))
    }

    @PatchMapping("{spaceId}/{historyId}/{feedbackId}")
    fun updateFeedback(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @PathVariable historyId: UUID,
        @PathVariable feedbackId: UUID,
        @Valid @RequestBody feedbackRequestDto: FeedbackRequestDto
    ): ResponseEntity<FeedbackResponseDto> {
        val feedback =
            feedbackService.updateFeedback(authentication, spaceId, historyId, feedbackId, feedbackRequestDto)
        return ResponseEntity.ok().body(responseDtoManager.generateFeedbackResponseDto(feedback))
    }

    @DeleteMapping("{spaceId}/{historyId}/{feedbackId}")
    fun deleteFeedback(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @PathVariable historyId: UUID,
        @PathVariable feedbackId: UUID
    ): ResponseEntity<Any> {
        feedbackService.deleteFeedback(authentication, spaceId, historyId, feedbackId)
        return ResponseEntity.noContent().build()
    }
}