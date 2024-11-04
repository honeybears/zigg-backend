package soma.achoom.zigg.board.service

import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.board.dto.BoardResponseDto
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.global.dto.PageInfo
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.repository.PostRepository

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val postRepository: PostRepository
) {
    @Transactional
    fun getBoards(authentication: Authentication):List<BoardResponseDto>{
        return boardRepository.findAll().map {
            BoardResponseDto(it.boardId!!, it.name)
        }
    }
}