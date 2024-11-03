package soma.achoom.zigg.comment.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.board.exception.BoardNotFoundException
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.comment.dto.CommentRequestDto
import soma.achoom.zigg.comment.dto.CommentResponseDto
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.comment.exception.CommentNotFoundException
import soma.achoom.zigg.comment.exception.CommentUserMissMatchException
import soma.achoom.zigg.comment.repository.CommentRepository
import soma.achoom.zigg.post.exception.PostNotFoundException
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.service.UserService

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val boardRepository: BoardRepository
){
    @Transactional(readOnly = false)
    fun createComment(authentication: Authentication,boardId:Long, postId:Long, commentRequestDto : CommentRequestDto) : CommentResponseDto{
        val user = userService.authenticationToUser(authentication)
        val board = boardRepository.findById(boardId).orElseThrow { BoardNotFoundException() }

        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException() }
        val comment = Comment(
            creator = user,
            textComment = commentRequestDto.message,
        )
        post.comments.add(comment)
        postRepository.save(post)
        return CommentResponseDto(
            commentId = comment.commentId,
            commentLike = comment.likes,
            commentMessage = comment.textComment,
            commentCreator = UserResponseDto(
                userId = user.userId,
                userName = user.name,
                userNickname = user.nickname,
                profileImageUrl = user.profileImageKey.imageKey,
            ),
            createdAt = comment.createAt,
        )
    }
    @Transactional(readOnly = false)
    fun createChildComment(authentication: Authentication,boardId:Long, postId:Long,commentId: Long, commentRequestDto: CommentRequestDto) : CommentResponseDto{
        val user = userService.authenticationToUser(authentication)
        val board = boardRepository.findById(boardId).orElseThrow { BoardNotFoundException() }
        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException() }

        val parentComment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException() }
        val childComment = Comment(
            creator = user,
            textComment = commentRequestDto.message,
        )
        parentComment.replies.add(childComment)
        commentRepository.save(parentComment)

        return CommentResponseDto(
            commentId = childComment.commentId,
            commentLike = childComment.likes,
            commentMessage = childComment.textComment,
            commentCreator = UserResponseDto(
                userId = user.userId,
                userName = user.name,
                userNickname = user.nickname,
                profileImageUrl = user.profileImageKey.imageKey,
            ),
            createdAt = childComment.createAt,
        )
    }
    @Transactional(readOnly = false)
    fun updateComment(authentication: Authentication,commentId:Long, commentRequestDto: CommentRequestDto) : CommentResponseDto{
        val user = userService.authenticationToUser(authentication)
        val comment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException() }
        if(comment.creator != user){
            throw CommentUserMissMatchException()
        }
        comment.textComment = commentRequestDto.message
        commentRepository.save(comment)
        return CommentResponseDto(
            commentId = comment.commentId,
            commentLike = comment.likes,
            commentMessage = comment.textComment,
            commentCreator = UserResponseDto(
                userId = user.userId,
                userName = user.name,
                userNickname = user.nickname,
                profileImageUrl = user.profileImageKey.imageKey,
            ),
            createdAt = comment.createAt,
            childComment = comment.replies.map {
                CommentResponseDto(
                    commentId = it.commentId,
                    commentLike = it.likes,
                    commentMessage = it.textComment,
                    commentCreator = UserResponseDto(
                        userId = it.creator.userId,
                        userName = it.creator.name,
                        userNickname = it.creator.nickname,
                        profileImageUrl = it.creator.profileImageKey.imageKey,
                    ),
                    createdAt = it.createAt,
                )
            }.toMutableList()
        )
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