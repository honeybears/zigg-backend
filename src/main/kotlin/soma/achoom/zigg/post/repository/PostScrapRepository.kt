package soma.achoom.zigg.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.entity.PostScrap
import soma.achoom.zigg.user.entity.User

interface PostScrapRepository : JpaRepository<PostScrap, Long> {
    fun findByUser(user: User): List<PostScrap>
    fun findByPost(post: Post): List<PostScrap>
    fun existsPostScrapByPostAndUser(post: Post, user: User): Boolean
    fun findByPostAndUser(post: Post, user: User): PostScrap?
    fun countPostScrapsByPost(post: Post): Long
}