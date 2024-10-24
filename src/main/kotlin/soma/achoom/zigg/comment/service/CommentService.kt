package soma.achoom.zigg.comment.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.board.exception.BoardNotFoundException
import soma.achoom.zigg.board.repository.BoardRepository
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
    private val userService: UserService,
    private val boardRepository: BoardRepository
){
    @Transactional(readOnly = false)
    fun createComment(authentication: Authentication,boardId:Long, postId:Long, commentRequestDto : CommentRequestDto) : Comment{
        val user = userService.authenticationToUser(authentication)
        val board = boardRepository.findById(boardId).orElseThrow { BoardNotFoundException() }

        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException() }
        val comment = Comment(
            creator = user,
            textComment = commentRequestDto.message,
        )
        post.comments.add(comment)
        postRepository.save(post)
        return comment
    }
    @Transactional(readOnly = false)
    fun createChildComment(authentication: Authentication,boardId:Long, postId:Long,commentId: Long, commentRequestDto: CommentRequestDto) : Comment{
        val user = userService.authenticationToUser(authentication)
        val board = boardRepository.findById(boardId).orElseThrow { BoardNotFoundException() }
        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException() }

        val parentComment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException() }
        val childComment = Comment(
            creator = user,
            textComment = commentRequestDto.message,
        )
        parentComment.replies.add(childComment)
        return commentRepository.save(parentComment)
    }
    @Transactional(readOnly = false)
    fun updateComment(authentication: Authentication,commentId:Long, commentRequestDto: CommentRequestDto) : Comment{
        val user = userService.authenticationToUser(authentication)
        val comment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException() }
        if(comment.creator != user){
            throw CommentUserMissMatchException()
        }
        comment.textComment = commentRequestDto.message
        return commentRepository.save(comment)
    }
    @Transactional(readOnly = false)
    fun deleteComment(authentication: Authentication,commentId: Long){
        val user = userService.authenticationToUser(authentication)
        val comment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException() }
        if(comment.creator != user){
            throw CommentUserMissMatchException()
        }
        comment.isDeleted = true
        commentRepository.save(comment)
    }

}