package soma.achoom.zigg.comment.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.comment.dto.CommentRequestDto
import soma.achoom.zigg.comment.dto.CommentResponseDto
import soma.achoom.zigg.comment.service.CommentService
import soma.achoom.zigg.global.ResponseDtoManager

@RestController
@RequestMapping("/api/v0/boards/posts/comments")
class CommentController(
    private val commentService: CommentService,
    private val responseDtoManager: ResponseDtoManager
) {
    @PostMapping("/{boardId}/{postId}/{commentId}")
    fun createChildComment(authentication: Authentication, @PathVariable boardId: Long, @PathVariable postId: Long, @PathVariable commentId: Long, commentRequestDto: CommentRequestDto) : ResponseEntity<CommentResponseDto>{
        val comment = commentService.createChildComment(authentication, boardId, postId, commentId, commentRequestDto)
        return ResponseEntity.ok(responseDtoManager.generateCommentResponseDto(comment))
    }
//    @GetMapping("/{boardId}/{postId}/{commentId}/likes")
//    fun likeComment(authentication: Authentication, @PathVariable boardId: Long, @PathVariable postId: Long, @PathVariable commentId: Long):ResponseEntity<CommentResponseDto> {
//        commentService.likeComment(authentication, boardId, postId, commentId)
//        return ResponseEntity.noContent().build()
//    }
//    @PostMapping("/{boardId}/{postId}")
//    fun createComment(authentication: Authentication, @PathVariable boardId: Long, @PathVariable postId: Long, commentRequestDto: CommentRequestDto) : ResponseEntity<CommentResponseDto>{
//        commentService.createComment(authentication,boardId, postId, commentRequestDto)
//    }
//    @PatchMapping("/{boardId}/{postId}/{commentId}")
//    fun updateComment(authentication: Authentication, @PathVariable boardId: Long, @PathVariable postId: Long, @PathVariable commentId: Long, commentRequestDto: CommentRequestDto) : ResponseEntity<CommentResponseDto>{
//        commentService.updateComment(authentication, commentId, commentRequestDto)
//    }
    @DeleteMapping("/{boardId}/{postId}/{commentId}")
    fun deleteComment(authentication: Authentication,@PathVariable boardId: Long, @PathVariable postId: Long, @PathVariable commentId: Long) : ResponseEntity<Void>{
        commentService.deleteComment(authentication, commentId)
        return ResponseEntity.noContent().build()
    }
}