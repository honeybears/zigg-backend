package soma.achoom.zigg.post.dto

import soma.achoom.zigg.comment.dto.CommentResponseDto
import soma.achoom.zigg.content.dto.ImageResponseDto
import soma.achoom.zigg.content.dto.VideoResponseDto

data class PostResponseDto(
    val postId: Long,
    val postTitle: String,
    val postMessage: String? = null,
    val postImageContents: List<ImageResponseDto> = listOf(),
    val postVideoContent: VideoResponseDto? = null,
    val postThumbnailImage: ImageResponseDto? = null,
    val comments : List<CommentResponseDto> = listOf(),
    val likeCnt: Int,
    val commentCnt: Int,
    val scrapCnt: Int,
    val isScraped: Boolean,
    val isLiked: Boolean
) {

}