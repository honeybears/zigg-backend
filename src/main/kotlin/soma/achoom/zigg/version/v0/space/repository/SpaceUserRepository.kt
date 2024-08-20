package soma.achoom.zigg.version.v0.space.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.version.v0.space.entity.Space
import soma.achoom.zigg.version.v0.space.entity.SpaceUser
import soma.achoom.zigg.version.v0.user.entity.User
import java.util.UUID

interface SpaceUserRepository : JpaRepository<SpaceUser,UUID> {
    fun findSpaceUsersByUser(user: User): List<SpaceUser>
    fun findSpaceUserBySpaceAndUser(space: Space, user: User): SpaceUser?
    fun findSpaceUserBySpaceUserId(spaceUserId: UUID): SpaceUser?
}