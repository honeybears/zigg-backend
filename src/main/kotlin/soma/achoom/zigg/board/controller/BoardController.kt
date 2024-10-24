package soma.achoom.zigg.board.controller

import org.springframework.http.ResponseEntity
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
    fun getBoards(authentication: Authentication)  : ResponseEntity<List<BoardResponseDto>> {
        return ResponseEntity.ok(boardService.getBoardList().map{BoardResponseDto(it.boardId!! , it.name)})
    }
    @GetMapping("/hot")
    fun getHotBoards(authentication: Authentication) : ResponseEntity<List<BoardResponseDto>>{
        return ResponseEntity.ok(boardService.getRecentHotPosts().map{BoardResponseDto(it.postId!!, it.title)})
    }
}