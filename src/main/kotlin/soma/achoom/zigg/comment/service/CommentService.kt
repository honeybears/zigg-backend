package soma.achoom.zigg.comment.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.comment.dto.CommentRequestDto
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.comment.exception.CommentNotFoundException
import soma.achoom.zigg.comment.exception.CommentUserMissMatchException
import soma.achoom.zigg.comment.repository.CommentRepository
import soma.achoom.zigg.post.exception.PostNotFoundException
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.user.service.UserService

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userService: UserService
){
    @Transactional(readOnly = false)
    fun createComment(authentication: Authentication, postId:Long, commentRequestDto : CommentRequestDto){
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException() }
        val comment = Comment(
            creator = user,
            textComment = commentRequestDto.message,
        )
        post.comments.add(comment)
        postRepository.save(post)
    }
    @Transactional(readOnly = false)
    fun createChildComment(authentication: Authentication,commentId: Long, commentRequestDto: CommentRequestDto){
        val user = userService.authenticationToUser(authentication)
        val parentComment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException() }
        val childComment = Comment(
            creator = user,
            textComment = commentRequestDto.message,
        )
        commentRepository.save(childComment)
    }
    @Transactional(readOnly = false)
    fun updateComment(authentication: Authentication,commentId:Long, commentRequestDto: CommentRequestDto){
        val user = userService.authenticationToUser(authentication)
        val comment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException() }
        if(comment.creator != user){
            throw CommentUserMissMatchException()
        }
        comment.textComment = commentRequestDto.message
        commentRepository.save(comment)
    }
    @Transactional(readOnly = false)
    fun deleteComment(authentication: Authentication,commentId: Long){
        val user = userService.authenticationToUser(authentication)
        val comment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException() }
        if(comment.creator != user){
            throw CommentUserMissMatchException()
        }
        commentRepository.delete(comment)
    }
}