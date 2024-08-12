package soma.achoom.zigg.v0.space.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import soma.achoom.zigg.v0.history.entity.History
import soma.achoom.zigg.v0.space.entity.Space
import soma.achoom.zigg.v0.space.entity.SpaceUser
import soma.achoom.zigg.v0.user.entity.User
import java.util.UUID


interface SpaceRepository : JpaRepository<Space, UUID> {

    fun findSpacesBySpaceUsersContaining(spaceUsers: SpaceUser): List<Space>
    fun findSpaceBySpaceId(spaceId: UUID): Space?
    fun findSpaceByHistoriesContains(history: History): Space?

    // @Query("select s from Space s where s in (select spaceUser.space from s.spaceUsers spaceUser where spaceUser.user = :user and spaceUser.inviteStatus = 'ACCEPT')")

    @Query("select s from Space s where s in (select spaceUser.space from SpaceUser spaceUser where spaceUser.user = :user and spaceUser.inviteStatus = 'ACCEPTED')")
    fun findSpaceByUserAndAccepted(user: User) : List<Space>

}