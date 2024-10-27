package soma.achoom.zigg.board.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.repository.PostRepository

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val postRepository: PostRepository
) {
    fun getBoardList(): List<Board> {
        return boardRepository.findAll()
    }

    fun getRecentHotPosts(): List<Post> {
        return postRepository.findBestPosts(Pageable.ofSize(2))
    }

}