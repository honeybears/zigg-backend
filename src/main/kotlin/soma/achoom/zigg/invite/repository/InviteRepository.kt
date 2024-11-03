package soma.achoom.zigg.invite.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.user.entity.User

interface InviteRepository : JpaRepository<Invite,Long>{
    fun findAllByInvitee(user:User):List<Invite>
    fun findInvitesBySpace(space: Space):List<Invite>
}