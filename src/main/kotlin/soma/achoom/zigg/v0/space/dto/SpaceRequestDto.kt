package soma.achoom.zigg.v0.space.dto

import soma.achoom.zigg.v0.space.entity.SpaceRole
import soma.achoom.zigg.v0.space.entity.SpaceUserStatus
import soma.achoom.zigg.v0.user.entity.User

import soma.achoom.zigg.v0.space.entity.Space
import soma.achoom.zigg.v0.space.entity.SpaceUser
import java.time.LocalDateTime
import java.util.UUID

data class SpaceRequestDto(
    val spaceId: UUID?,
    val spaceName: String,
    val spaceUsers: List<SpaceUserRequestDto>,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
) {

}
