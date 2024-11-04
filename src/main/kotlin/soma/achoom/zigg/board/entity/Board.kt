package soma.achoom.zigg.board.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity

@Entity(name = "board")
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var boardId: Long? = null,
    @Column(name = "board_name")
    val name:String,

): BaseEntity()