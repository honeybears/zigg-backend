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

    fun toSpace(admin: User, invitedUser: MutableSet<User>): Space {
        val space = Space(
            spaceId = null,
            spaceName = spaceName,
            spaceUsers = mutableSetOf(),
            histories = mutableSetOf(),
        )
        space.spaceUsers.add(
            SpaceUser(
                space = space,
                user = admin,
                spaceRole = SpaceRole.ADMIN,
                feedbackRecipients = mutableSetOf(),
                inviteStatus = SpaceUserStatus.ACCEPTED,
                spaceUserId = null
            )
        )
        invitedUser.forEach {
            println(it.userNickname)
            val spaceUser = SpaceUser(
                space = space,
                user = it,
                spaceRole = SpaceRole.USER,
                feedbackRecipients = mutableSetOf(),
                inviteStatus = SpaceUserStatus.ACCEPTED,
                spaceUserId = null
            )
            space.spaceUsers.add(
                spaceUser
            )
        }
        println(space.spaceUsers.count())
        return space
    }

}
