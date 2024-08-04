package soma.achoom.zigg.v0.exception

import com.amazonaws.services.kms.model.AlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(e: UserAlreadyExistsException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
    @ExceptionHandler(AlreadyExistsException::class)
    fun handleAlreadyExistsException(e: AlreadyExistsException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }
    @ExceptionHandler(SpaceNotFoundException::class)
    fun handleUnknownSpace(e: SpaceNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
    @ExceptionHandler(HistoryNotFoundException::class)
    fun handleUnknownHistory(e: HistoryNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
    @ExceptionHandler(FeedbackNotFoundException::class)
    fun handleUnKnowFeedback(e: FeedbackNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
    @ExceptionHandler(AlreadyExistsSpaceUserException::class)
    fun handleAlreadyExistsSpaceUser(e: AlreadyExistsSpaceUserException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }
    @ExceptionHandler(LowSpacePermissionException::class)
    fun handleLowSpacePermission(e: LowSpacePermissionException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
    }
    @ExceptionHandler(SpaceUserNotFoundInSpaceException::class)
    fun handlerNoExistsSpaceUser(e: SpaceUserNotFoundInSpaceException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}