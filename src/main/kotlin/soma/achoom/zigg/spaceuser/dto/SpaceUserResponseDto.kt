package soma.achoom.zigg.spaceuser.dto

import soma.achoom.zigg.spaceuser.entity.SpaceRole
import java.util.UUID

data class SpaceUserResponseDto(
    val userNickname: String?,
    val userName:String?,
    val spaceUserId: UUID?,
    val spaceRole: SpaceRole?,
    val profileImageUrl: String?
)
