package soma.achoom.zigg.v0.controller

import com.amazonaws.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.dto.request.FeedbackRequestDto
import soma.achoom.zigg.v0.dto.response.FeedbackResponseDto
import soma.achoom.zigg.v0.service.FeedbackService

@RestController
@RequestMapping("/api/v0/spaces/histories/feedback")
class FeedbackController @Autowired constructor(private val feedbackService: FeedbackService) {
    @GetMapping("{spaceId}/{historyId}")
    fun getFeedbacks(authentication: Authentication,@PathVariable spaceId: Long, @PathVariable historyId: Long) : ResponseEntity<List<FeedbackResponseDto>> {
        val feedbackResponseDto = feedbackService.getFeedbacks(authentication, spaceId, historyId)
        return ResponseEntity.ok().body(feedbackResponseDto)
    }
    @PostMapping("{spaceId}/{historyId}")
    fun createFeedback(authentication: Authentication,@PathVariable spaceId: Long, @PathVariable historyId: Long, feedbackRequestDto: FeedbackRequestDto) : ResponseEntity<FeedbackResponseDto> {
        val feedbackResponseDto = feedbackService.createFeedback(authentication, spaceId, historyId,feedbackRequestDto)
        return ResponseEntity.ok().body(feedbackResponseDto)
    }
    @PatchMapping("{spaceId}/{historyId}/{feedbackId}")
    fun updateFeedback(authentication: Authentication,@PathVariable spaceId: Long, @PathVariable historyId: Long, @PathVariable feedbackId: Long, feedbackRequestDto: FeedbackRequestDto) : ResponseEntity<FeedbackResponseDto> {
        val feedbackResponseDto = feedbackService.updateFeedback(authentication, spaceId, historyId, feedbackId, feedbackRequestDto)
        return ResponseEntity.ok().body(feedbackResponseDto)
    }
    @DeleteMapping("{spaceId}/{historyId}/{feedbackId}")
    fun deleteFeedback(authentication: Authentication,@PathVariable spaceId: Long, @PathVariable historyId: Long, @PathVariable feedbackId: Long) : ResponseEntity<Any> {
        feedbackService.deleteFeedback(authentication, spaceId, historyId, feedbackId)
        return ResponseEntity.noContent().build()
    }
}