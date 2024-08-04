package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser
import soma.achoom.zigg.v0.model.User


interface SpaceRepository : JpaRepository<Space, Long> {

    fun findSpacesBySpaceUsersContaining(spaceUsers: SpaceUser): List<Space>
    fun findSpaceBySpaceId(spaceId: Long): Space?
    fun findSpaceByHistoriesContains(history: History): Space?

    @Query("select s from Space s where s in (select spaceUser.space from s.spaceUsers spaceUser where spaceUser.user = :user)")
    fun findSpaceByUser(user:User) : List<Space>

}