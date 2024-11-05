package soma.achoom.zigg.comment.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.user.dto.UserResponseDto
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CommentResponseDto(
    val commentId: Long?,
    val commentMessage: String,
    val commentLike: Int,
    val commentCreator : UserResponseDto,
    val createdAt: LocalDateTime,
    val parentComment:CommentResponseDto? = null,
    val childComment:MutableList<CommentResponseDto>? = null
) {
}