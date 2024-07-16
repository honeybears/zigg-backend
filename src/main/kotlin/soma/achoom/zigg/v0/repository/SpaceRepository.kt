package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.Space

interface SpaceRepository : JpaRepository<Space, Long> {

}