package soma.achoom.zigg.space.dto

import soma.achoom.zigg.spaceuser.dto.SpaceUserRequestDto

data class InviteUsersRequestDto(
    val spaceUsers : List<SpaceUserRequestDto>
)
