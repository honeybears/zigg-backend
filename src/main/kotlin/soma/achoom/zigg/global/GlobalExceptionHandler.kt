package soma.achoom.zigg.global

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import soma.achoom.zigg.comment.exception.CommentNotFoundException
import soma.achoom.zigg.comment.exception.CommentUserMissMatchException
import soma.achoom.zigg.content.exception.ImageNotfoundException
import soma.achoom.zigg.feedback.exception.FeedbackNotFoundException
import soma.achoom.zigg.firebase.exception.FCMMessagingFailException
import soma.achoom.zigg.history.exception.GuestHistoryCreateLimitationException
import soma.achoom.zigg.history.exception.HistoryNotFoundException
import soma.achoom.zigg.invite.exception.InviteExpiredException
import soma.achoom.zigg.invite.exception.InviteNotFoundException
import soma.achoom.zigg.invite.exception.InvitedUserMissMatchException
import soma.achoom.zigg.invite.exception.UserAlreadyInSpaceException
import soma.achoom.zigg.post.exception.PostCreatorMismatchException
import soma.achoom.zigg.post.exception.PostNotFoundException
import soma.achoom.zigg.space.exception.*
import soma.achoom.zigg.user.exception.GuestUserUpdateProfileLimitationException
import soma.achoom.zigg.user.exception.NicknameUserNotFoundException
import soma.achoom.zigg.user.exception.UserAlreadyExistsException
import soma.achoom.zigg.user.exception.UserNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(e: UserAlreadyExistsException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
    }
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
    }
    @ExceptionHandler(SpaceNotFoundException::class)
    fun handleUnknownSpace(e: SpaceNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(HistoryNotFoundException::class)
    fun handleUnknownHistory(e: HistoryNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(FeedbackNotFoundException::class)
    fun handleUnKnowFeedback(e: FeedbackNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(AlreadyExistsSpaceUserException::class)
    fun handleAlreadyExistsSpaceUser(e: AlreadyExistsSpaceUserException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
    }
    @ExceptionHandler(LowSpacePermissionException::class)
    fun handleLowSpacePermission(e: LowSpacePermissionException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.message)
    }
    @ExceptionHandler(SpaceUserNotFoundInSpaceException::class)
    fun handlerNoExistsSpaceUser(e: SpaceUserNotFoundInSpaceException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(NicknameUserNotFoundException::class)
    fun handleNicknameUserNotFound(e: NicknameUserNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(FCMMessagingFailException::class)
    fun handleFCMMessagingFail(e: FCMMessagingFailException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
    }
    @ExceptionHandler(InviteNotFoundException::class)
    fun handleInviteNotFound(e: InviteNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(InvitedUserMissMatchException::class)
    fun handleInvitedUserMissMatch(e: InvitedUserMissMatchException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
    }
    @ExceptionHandler(InviteExpiredException::class)
    fun handleInviteExpired(e: InviteExpiredException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
    }
    @ExceptionHandler(UserAlreadyInSpaceException::class)
    fun handleUserAlreadyInSpace(e: UserAlreadyInSpaceException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
    }
    @ExceptionHandler(GuestSpaceCreateLimitationException::class)
    fun handleGuestSpaceCreateLimitation(e: GuestSpaceCreateLimitationException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
    }
    @ExceptionHandler(GuestHistoryCreateLimitationException::class)
    fun handleGuestHistoryCreateLimitation(e: GuestHistoryCreateLimitationException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
    }
    @ExceptionHandler(GuestUserUpdateProfileLimitationException::class)
    fun handleGuestUserUpdateProfileLimitation(e: GuestUserUpdateProfileLimitationException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
    }
    @ExceptionHandler(PostCreatorMismatchException::class)
    fun handlePostCreatorMismatch(e: PostCreatorMismatchException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
    }
    @ExceptionHandler(PostNotFoundException::class)
    fun handlePostNotFound(e: PostNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(CommentNotFoundException::class)
    fun handleCommentNotFound(e: CommentNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
    @ExceptionHandler(CommentUserMissMatchException::class)
    fun handleCommentUserMissMatch(e: CommentUserMissMatchException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
    }
    @ExceptionHandler(ImageNotfoundException::class)
    fun handleImageNotFound(e: ImageNotfoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
}