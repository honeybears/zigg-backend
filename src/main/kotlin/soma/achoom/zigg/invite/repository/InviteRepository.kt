package soma.achoom.zigg.invite.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.user.entity.User
import java.util.UUID

interface InviteRepository : JpaRepository<Invite,UUID>{
    fun findAllByUser(user:User):List<Invite>
}