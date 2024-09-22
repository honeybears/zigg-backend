package soma.achoom.zigg.content.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.content.entity.Video

interface VideoRepository : JpaRepository<Video,Long> {
}