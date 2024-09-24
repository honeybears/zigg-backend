package soma.achoom.zigg.post.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.post.entity.Post

interface PostRepository : JpaRepository<Post,Long>{
    override fun findAll(page: Pageable): Page<Post>

    fun findPostsByPostTitleContaining(keyword: String, page: Pageable): Page<Post>
}