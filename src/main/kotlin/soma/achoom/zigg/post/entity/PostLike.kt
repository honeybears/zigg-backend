package soma.achoom.zigg.post.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User

@Entity
@EntityListeners(PostLikeEntityListener::class)
class PostLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(cascade = [CascadeType.MERGE])
    val user: User,

    @ManyToOne(cascade = [CascadeType.MERGE])
    val post: Post
) : BaseEntity() {
}
