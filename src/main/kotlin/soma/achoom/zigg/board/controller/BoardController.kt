package soma.achoom.zigg.board.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.board.dto.BoardResponseDto
import soma.achoom.zigg.board.service.BoardService

@RestController
@RequestMapping("/api/v0/boards")
class BoardController(
    private val boardService: BoardService
) {
    @GetMapping
    fun getBoards(authentication: Authentication)  : List<BoardResponseDto>{
        return boardService.getBoardList().map{BoardResponseDto(it.boardId!! , it.name)}
    }
    @GetMapping("/hot")
    fun getHotBoards(authentication: Authentication) : List<BoardResponseDto>{
        return boardService.getRecentHotPosts().map{BoardResponseDto(it.postId!!, it.title)}
    }
}