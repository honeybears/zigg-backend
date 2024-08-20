package soma.achoom.zigg.space.dto

import soma.achoom.zigg.space.entity.SpaceRole
import java.util.UUID

data class SpaceUserResponseDto(
    val userNickname: String?,
    val userName:String?,
    val spaceUserId: UUID?,
    val spaceRole: SpaceRole?
)
