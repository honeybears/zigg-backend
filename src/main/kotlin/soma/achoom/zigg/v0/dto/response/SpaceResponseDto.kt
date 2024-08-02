package soma.achoom.zigg.v0.dto.response

import soma.achoom.zigg.v0.dto.BaseResponseDto
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser

data class SpaceResponseDto(
    val spaceId: Long?,
    val spaceName: String?,

    val spaceUsers: MutableSet<SpaceUser>?,
    val history: MutableSet<History>?,

    ) : BaseResponseDto() {
    companion object {
        fun from(space: Space): SpaceResponseDto {
            return SpaceResponseDto(
                spaceId = space.spaceId,
                spaceName = space.spaceName,
                spaceUsers = space.spaceUsers,
                history = space.histories
            )
        }
    }
}
