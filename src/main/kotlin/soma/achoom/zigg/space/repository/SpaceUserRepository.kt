package soma.achoom.zigg.space.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.user.entity.User
import java.util.UUID

interface SpaceUserRepository : JpaRepository<SpaceUser, Long> {
    fun findSpaceUsersByUser(user: User): List<SpaceUser>

    fun findSpaceUserBySpaceUserId(spaceUserId: Long): SpaceUser?

    fun findSpaceUserBySpace(space: Space): List<SpaceUser>

    fun deleteAllBySpace(space: Space)

    fun findSpaceUserBySpaceAndUser(space: Space, user: User): SpaceUser?

    fun existsSpaceUserBySpaceAndUser(space: Space, user: User): Boolean
}
