package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser
import soma.achoom.zigg.v0.model.User
import java.util.UUID

interface SpaceUserRepository : JpaRepository<SpaceUser,UUID> {
    fun findSpaceUsersByUser(user: User): List<SpaceUser>
    fun findSpaceUserBySpaceAndUser(space: Space, user: User): SpaceUser?
    fun findSpaceUserBySpaceUserId(spaceUserId: UUID): SpaceUser?
}