package soma.achoom.zigg.v0.exception

import com.amazonaws.services.kms.model.AlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
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
    @ExceptionHandler(UnknownSpace::class)
    fun handleUnknownSpace(e: UnknownSpace): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
    @ExceptionHandler(UnknownHistory::class)
    fun handleUnknownHistory(e: UnknownHistory): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
    @ExceptionHandler(UnKnownFeedback::class)
    fun handleUnKnowFeedback(e: UnKnownFeedback): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
    @ExceptionHandler(AlreadyExistsSpaceUser::class)
    fun handleAlreadyExistsSpaceUser(e: AlreadyExistsSpaceUser): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }
    @ExceptionHandler(LowSpacePermission::class)
    fun handleLowSpacePermission(e: LowSpacePermission): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
    }
    @ExceptionHandler(NoExistsSpaceUser::class)
    fun handlerNoExistsSpaceUser(e: NoExistsSpaceUser): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}