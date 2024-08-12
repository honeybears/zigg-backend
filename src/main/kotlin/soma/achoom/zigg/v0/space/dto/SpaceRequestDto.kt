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

    fun toSpace(admin: User, invitedUser: MutableSet<User>, spaceImageUrl:String): Space {
        val space = Space(
            spaceName = spaceName,
            spaceImageUrl = spaceImageUrl
        )
        space.spaceUsers.add(
            SpaceUser(
                space = space,
                user = admin,
                spaceRole = SpaceRole.ADMIN,
                inviteStatus = SpaceUserStatus.ACCEPTED,
            )
        )
        invitedUser.forEach {
            space.spaceUsers.add(
                SpaceUser(
                space = space,
                user = it,
                spaceRole = SpaceRole.USER, // Use correct role
                inviteStatus = SpaceUserStatus.ACCEPTED,
            )
            )

        }
        return space
    }

}
