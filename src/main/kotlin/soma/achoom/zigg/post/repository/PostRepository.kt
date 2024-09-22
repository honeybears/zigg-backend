package soma.achoom.zigg.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.post.entity.Post

interface PostRepository : JpaRepository<Post,Long>{
}