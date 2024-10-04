package soma.achoom.zigg.content.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.user.entity.User

interface ImageRepository : JpaRepository<Image,Long>{
    fun findByImageKey(imageKey: String) : Image?
    fun findImagesByUploader(uploader: User) : List<Image>
}