package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser

interface SpaceRepository : JpaRepository<Space, Long> {

    fun findSpacesBySpaceUsersContaining(spaceUsers: SpaceUser): List<Space>
    fun findSpaceBySpaceId(spaceId: Long): Space?
    fun findSpaceByHistoriesContains(history: History): Space?
}