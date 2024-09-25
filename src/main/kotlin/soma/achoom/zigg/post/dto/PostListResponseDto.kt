package soma.achoom.zigg.post.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.global.dto.PageInfo

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PostListResponseDto(
    val posts : List<PostResponseDto>,
    val pageInfo: PageInfo
)