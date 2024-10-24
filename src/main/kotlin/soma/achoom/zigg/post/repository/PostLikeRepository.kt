package soma.achoom.zigg.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.post.entity.PostLike

interface PostLikeRepository : JpaRepository<PostLike, Long> {
}