package soma.achoom.zigg.feedback.controller

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import soma.achoom.zigg.feedback.dto.FeedbackRequestDto
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.feedback.service.FeedbackService

@RestController
@RequestMapping("/api/v0/spaces/histories/feedbacks")
class FeedbackController @Autowired constructor(
    private val feedbackService: FeedbackService,
) {

    @GetMapping("{spaceId}/{historyId}")
    fun getFeedbacks(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long): ResponseEntity<List<FeedbackResponseDto>> {
        val feedbacks = feedbackService.getFeedbacks(authentication, spaceId, historyId)
        return ResponseEntity.ok().body(feedbacks)
    }

    @PostMapping("{spaceId}/{historyId}")
    fun createFeedback(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long, @Valid @RequestBody feedbackRequestDto: FeedbackRequestDto): ResponseEntity<FeedbackResponseDto> {
        val feedback = feedbackService.createFeedback(authentication, spaceId, historyId, feedbackRequestDto)
        return ResponseEntity.ok().body(feedback)
    }

    @PatchMapping("{spaceId}/{historyId}/{feedbackId}")
    fun updateFeedback(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long, @PathVariable feedbackId: Long, @Valid @RequestBody feedbackRequestDto: FeedbackRequestDto): ResponseEntity<FeedbackResponseDto> {
        val feedback =
            feedbackService.updateFeedback(authentication, spaceId, historyId, feedbackId, feedbackRequestDto)
        return ResponseEntity.ok().body(feedback)
    }

    @DeleteMapping("{spaceId}/{historyId}/{feedbackId}")
    fun deleteFeedback(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long, @PathVariable feedbackId: Long): ResponseEntity<Any> {
        feedbackService.deleteFeedback(authentication, spaceId, historyId, feedbackId)
        return ResponseEntity.noContent().build()
    }
}