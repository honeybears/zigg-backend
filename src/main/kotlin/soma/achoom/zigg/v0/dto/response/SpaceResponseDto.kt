package soma.achoom.zigg.v0.dto.response

import soma.achoom.zigg.v0.dto.BaseResponseDto
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser
import java.util.UUID

data class SpaceResponseDto(
    val spaceId: UUID?,
    val spaceName: String?,
    val spaceImageUrl: String?,
    val spaceUsers: MutableSet<SpaceUser>?,
    val history: MutableSet<HistoryResponseDto> = mutableSetOf(),

    ) : BaseResponseDto() {
    companion object {
        fun from(space: Space): SpaceResponseDto {
            return SpaceResponseDto(
                spaceId = space.spaceId,
                spaceName = space.spaceName,
                spaceUsers = space.spaceUsers,
                spaceImageUrl = space.spaceImageUrl,
                history = space.histories.map { HistoryResponseDto.from(it) }.toMutableSet()
            )
        }
    }
}
