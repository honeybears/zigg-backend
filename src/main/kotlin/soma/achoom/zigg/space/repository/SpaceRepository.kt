package soma.achoom.zigg.space.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.user.entity.User
import java.util.UUID


interface SpaceRepository : JpaRepository<Space, UUID> {

    fun findSpacesBySpaceUsersContaining(spaceUsers: SpaceUser): List<Space>
    fun findSpaceBySpaceId(spaceId: UUID): Space?
    fun findSpaceByHistoriesContains(history: History): Space?
    @Query("select s from Space s where s in (select spaceUser.space from SpaceUser spaceUser where spaceUser.user = :user)")
    fun findSpacesByUser(user: User) : List<Space>

}