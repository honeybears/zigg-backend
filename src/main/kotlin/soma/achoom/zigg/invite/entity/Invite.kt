package soma.achoom.zigg.invite.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.user.entity.User
import java.util.UUID

@Entity
@Table(name = "`invite`")
data class Invite(
    @Id
    val inviteId: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "space")
    val space: Space,

    @ManyToOne
    @JoinColumn(name = "invitee")
    val invitee: User,

    @ManyToOne
    @JoinColumn(name = "inviter")
    val inviter:User,

    @Enumerated(EnumType.STRING)
    var inviteStatus: InviteStatus = InviteStatus.WAITING,

    var isExpired: Boolean = false
) : BaseEntity() {
    override fun toString(): String {
        return "SpaceInvite(\n" +
                "id=$inviteId,\n" +
                "space=$space,\n" +
                "user=$invitee\n" +
                "inviteStatus=${inviteStatus.name}\n" +
                ")\n"
    }

    override fun hashCode(): Int {
        return inviteId.hashCode() + space.hashCode() + invitee.hashCode() + inviteStatus.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val invite = other as Invite
        return inviteId == invite.inviteId && space == invite.space && invitee == invite.invitee && inviteStatus == invite.inviteStatus
    }
}