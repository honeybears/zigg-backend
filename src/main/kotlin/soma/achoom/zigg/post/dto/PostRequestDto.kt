package soma.achoom.zigg.post.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.content.dto.VideoRequestDto


@JsonInclude(JsonInclude.Include.NON_NULL)
data class PostRequestDto(
    val postTitle: String,
    val postMessage: String,
    val postImageContent: MutableSet<String> = mutableSetOf(),
    val postVideoContent: MutableSet<VideoRequestDto> = mutableSetOf()
) {
}