package soma.achoom.zigg.comment.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.comment.dto.CommentRequestDto
import soma.achoom.zigg.comment.service.CommentService
import soma.achoom.zigg.global.ResponseDtoManager

@RestController
@RequestMapping("/api/v0")
class CommentController(
    private val commentService: CommentService,
    private val responseDtoManager: ResponseDtoManager
) {
    @PostMapping("/posts/comments/{postId}")
    fun createComment(authentication: Authentication, postId: Long, commentRequestDto: CommentRequestDto) {
        commentService.createComment(authentication, postId, commentRequestDto)
    }
}