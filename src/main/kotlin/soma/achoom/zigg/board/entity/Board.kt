package soma.achoom.zigg.board.entity

import jakarta.persistence.*
import soma.achoom.zigg.post.entity.Post

@Entity
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var boardId: Long? = null,

    val name:String,

    @OneToMany(fetch = FetchType.LAZY,orphanRemoval = true)
    val posts: MutableSet<Post> = mutableSetOf()
)