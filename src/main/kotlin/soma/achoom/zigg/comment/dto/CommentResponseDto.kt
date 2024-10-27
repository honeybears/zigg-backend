package soma.achoom.zigg.comment.dto

import soma.achoom.zigg.user.dto.UserResponseDto

data class CommentResponseDto(
    val commentId: Long,
    val commentMessage: String,
    val commentLike: Int,
    val commentCreator : UserResponseDto
) {
}