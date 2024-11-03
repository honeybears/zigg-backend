package soma.achoom.zigg.invite.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.user.dto.UserResponseDto
import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class InviteResponseDto(
    val inviteId:Long?,
    val space: SpaceResponseDto? = null,
    val inviter:UserResponseDto,
    val invitedUser:UserResponseDto,
    val createdAt: LocalDateTime,

    ) {
    override fun toString(): String {
        return "InviteResponseDto(\n" +
                "inviteId=$inviteId,\n" +
                "space=${space?.spaceId.toString()},\n" +
                "inviter=${inviter.toString()},\n" +
                "invitedUser=${invitedUser.toString()},\n" +
                "createdAt=$createdAt\n" +
                ")\n"
    }
}