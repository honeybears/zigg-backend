package soma.achoom.zigg.space.dto

import java.util.UUID

data class SpaceRequestDto(
    val spaceId: Long?,
    val spaceName: String,
    val spaceUsers: List<SpaceUserRequestDto>,
    val spaceImageUrl: String?,
)
