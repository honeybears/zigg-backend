package soma.achoom.zigg.space.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.user.entity.User
import java.util.UUID

interface SpaceUserRepository : JpaRepository<SpaceUser, UUID> {
    fun findSpaceUsersByUser(user: User): List<SpaceUser>
    fun findSpaceUserBySpaceAndUser(space: soma.achoom.zigg.space.entity.Space, user: User): SpaceUser?
    fun findSpaceUserBySpaceUserId(spaceUserId: UUID): SpaceUser?
    @Query("SELECT CASE WHEN EXISTS(SELECT SU FROM SpaceUser SU WHERE SU.space = :space AND SU.user.userName = :userName) THEN true ELSE false END")
    fun existsSpaceUserBySpaceAndUserName(space: Space, userName: String): Boolean
    fun deleteSpaceUserBySpaceAndUser(space: Space, user: User)
}