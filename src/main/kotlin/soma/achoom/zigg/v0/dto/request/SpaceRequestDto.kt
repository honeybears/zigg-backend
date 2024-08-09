package soma.achoom.zigg.v0.dto.request

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.jetbrains.annotations.NotNull
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.model.enums.SpaceRole
import soma.achoom.zigg.v0.model.enums.SpaceUserStatus
import java.time.LocalDateTime

data class SpaceRequestDto(
    val spaceId: Long?,
    val spaceName: String?,
    val spaceUsers: Set<SpaceUserRequestDto>,

    val createdAt: LocalDateTime? = LocalDateTime.now(),
) {

    fun toSpace(admin: User, invitedUser: MutableSet<User>,spaceImageUrl:String): Space {
        val space = Space(
            spaceName = spaceName,
            spaceId = null,
            spaceImageUrl = spaceImageUrl
        )
        space.spaceUsers.add(
            SpaceUser(
                space = space,
                user = admin,
                spaceRole = SpaceRole.ADMIN,
                inviteStatus = SpaceUserStatus.ACCEPTED,
                spaceUserId = null
            )
        )
        invitedUser.forEach {
            space.spaceUsers.add(SpaceUser(
                space = space,
                user = it,
                spaceRole = SpaceRole.USER, // Use correct role
                inviteStatus = SpaceUserStatus.ACCEPTED,
                spaceUserId = null
            ))

        }
        return space
    }

}
