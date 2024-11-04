package soma.achoom.zigg.content.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.user.entity.User

interface VideoRepository : JpaRepository<Video,Long> {
    fun findVideosByUploader(uploader: User) : List<Video>
}