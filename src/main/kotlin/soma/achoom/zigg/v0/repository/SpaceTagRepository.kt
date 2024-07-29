package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceTag

interface SpaceTagRepository : JpaRepository<SpaceTag, Long> {
    fun findSpaceTagsBySpace(space: Space): List<SpaceTag>
}