package soma.achoom.zigg.comment.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.comment.dto.CommentRequestDto
import soma.achoom.zigg.comment.dto.CommentResponseDto
import soma.achoom.zigg.comment.service.CommentService

@RestController
@RequestMapping("/api/v0/boards/posts/comments")
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping("/{boardId}/{postId}")
    fun createComment(
        authentication: Authentication,
        @PathVariable boardId: Long,
        @PathVariable postId: Long,
        @RequestBody commentRequestDto: CommentRequestDto
    ): ResponseEntity<CommentResponseDto> {
        val comment = commentService.createComment(authentication, boardId, postId, commentRequestDto)
        return ResponseEntity.ok(comment)
    }
    @PostMapping("/{boardId}/{postId}/{commentId}")
    fun createChildComment(
        authentication: Authentication,
        @PathVariable boardId: Long,
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @RequestBody commentRequestDto: CommentRequestDto
    ): ResponseEntity<CommentResponseDto> {
        val comment = commentService.createChildComment(authentication, boardId, postId, commentId, commentRequestDto)
        return ResponseEntity.ok(comment)
    }
    @PatchMapping("/{boardId}/{postId}/{commentId}")
    fun updateComment(
        authentication: Authentication,
        @PathVariable boardId: Long,
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @RequestBody commentRequestDto: CommentRequestDto
    ): ResponseEntity<CommentResponseDto> {
        val comment = commentService.updateComment(authentication, commentId, commentRequestDto)
        return ResponseEntity.ok(comment)
    }

    @DeleteMapping("/{boardId}/{postId}/{commentId}")
    fun deleteComment(
        authentication: Authentication,
        @PathVariable boardId: Long,
        @PathVariable postId: Long,
        @PathVariable commentId: Long
    ): ResponseEntity<Void> {
        commentService.deleteComment(authentication, boardId,postId,commentId)
        return ResponseEntity.noContent().build()
    }
    @GetMapping("likes/{boardId}/{postId}/{commentId}")
    fun getCommentLikes(
        authentication: Authentication,
        @PathVariable boardId: Long,
        @PathVariable postId: Long,
        @PathVariable commentId: Long
    ): ResponseEntity<CommentResponseDto> {
        val commentLikes = commentService.likeOrUnlikeComment(authentication,boardId, postId, commentId)
        return ResponseEntity.ok(commentLikes)
    }
}