package soma.achoom.zigg.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.post.entity.PostScrap

interface PostScrapRepository : JpaRepository<PostScrap, Long> {

}