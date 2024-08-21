package soma.achoom.zigg.space.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.spaceuser.entity.SpaceUser
import soma.achoom.zigg.user.entity.User
import java.util.UUID


interface SpaceRepository : JpaRepository<Space, UUID> {

    fun findSpacesBySpaceUsersContaining(spaceUsers: SpaceUser): List<soma.achoom.zigg.space.entity.Space>
    fun findSpaceBySpaceId(spaceId: UUID): soma.achoom.zigg.space.entity.Space?
    fun findSpaceByHistoriesContains(history: History): soma.achoom.zigg.space.entity.Space?
    @Query("select s from Space s where s in (select spaceUser.space from SpaceUser spaceUser where spaceUser.user = :user and spaceUser.inviteStatus = 'ACCEPTED')")
    fun findSpaceByUserAndAccepted(user: User) : List<soma.achoom.zigg.space.entity.Space>

}