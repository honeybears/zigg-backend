package soma.achoom.zigg.post.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.comment.dto.CommentResponseDto
import soma.achoom.zigg.content.dto.ImageResponseDto
import soma.achoom.zigg.content.dto.VideoResponseDto

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PostResponseDto(
    val postId: Long,
    val postTitle: String,
    val postMessage: String? = null,
    val postImageContents: List<ImageResponseDto>? = null,
    val postVideoContent: VideoResponseDto? = null,
    val postThumbnailImage: ImageResponseDto? = null,
    val comments : List<CommentResponseDto>? = null,
    val likeCnt: Long,
    val commentCnt: Long,
    val scrapCnt: Long,
    val isScraped: Boolean,
    val isLiked: Boolean
) {

}