package soma.achoom.zigg.global

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import soma.achoom.zigg.feedback.exception.FeedbackNotFoundException
import soma.achoom.zigg.history.exception.HistoryNotFoundException
import soma.achoom.zigg.space.exception.AlreadyExistsSpaceUserException
import soma.achoom.zigg.space.exception.LowSpacePermissionException
import soma.achoom.zigg.space.exception.SpaceNotFoundException
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
    @ExceptionHandler(soma.achoom.zigg.space.exception.SpaceUserNotFoundInSpaceException::class)
    fun handlerNoExistsSpaceUser(e: soma.achoom.zigg.space.exception.SpaceUserNotFoundInSpaceException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
}