package soma.achoom.zigg.board.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.board.entity.Board

interface BoardRepository : JpaRepository<Board,Long> {
}