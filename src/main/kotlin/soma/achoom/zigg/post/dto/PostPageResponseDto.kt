package soma.achoom.zigg.post.dto

import soma.achoom.zigg.global.dto.PageInfo

data class PostPageResponseDto(
    val posts : List<PostResponseDto>,
    val pageInfo: PageInfo
) {
}