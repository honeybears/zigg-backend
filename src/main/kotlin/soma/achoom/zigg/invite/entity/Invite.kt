package soma.achoom.zigg.invite.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.user.entity.User
import java.util.UUID

@Entity
@Table(name = "`invite`")
class Invite(
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


}