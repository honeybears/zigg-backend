package soma.achoom.zigg.post.dto

import soma.achoom.zigg.comment.dto.CommentResponseDto
import soma.achoom.zigg.content.dto.VideoResponseDto

data class PostResponseDto(
    val postId: Long,
    val postTitle: String,
    val postMessage: String? = null,
    val postImageContent: List<String> = listOf(),
    val postVideoContent: List<VideoResponseDto> = listOf(),
    val comments : List<CommentResponseDto> = listOf()

) {

}