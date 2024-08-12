package soma.achoom.zigg.v0.space.dto

import soma.achoom.zigg.v0.history.dto.HistoryResponseDto
import soma.achoom.zigg.v0.space.entity.Space
import soma.achoom.zigg.v0.space.entity.SpaceUser
import java.util.UUID

data class SpaceResponseDto(
    val spaceId: UUID?,
    val spaceName: String?,
    val spaceImageUrl: String?,
    val spaceUsers: MutableSet<SpaceUser>?,
    val history: MutableSet<HistoryResponseDto> = mutableSetOf(),

    ) {


}
