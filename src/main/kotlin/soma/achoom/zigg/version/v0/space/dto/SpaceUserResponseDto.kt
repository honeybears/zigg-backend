package soma.achoom.zigg.version.v0.space.dto

import soma.achoom.zigg.version.v0.space.entity.SpaceRole
import java.util.UUID

data class SpaceUserResponseDto(
    val userNickname: String?,
    val userName:String?,
    val spaceUserId: UUID?,
    val spaceRole: SpaceRole?
)
