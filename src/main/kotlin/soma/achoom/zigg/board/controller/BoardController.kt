package soma.achoom.zigg.board.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.board.service.BoardService

@RestController
@RequestMapping("/api/v0/boards")
class BoardController(
    private val boardService: BoardService
) {
    @GetMapping
    fun getBoardList() {
        println("getBoardList")
    }

//    @PostMapping("/posts/{boardId}")
//    fun createPost(authentication: Authentication, @PathVariable boardId: Long) {
//        println("createPost")
//    }
//
//    @GetMapping("/posts/{boardId}")
//    fun getBoardPosts(@PathVariable boardId: Long, @RequestParam("page") page: Long) {
//        println("getBoardPosts")
//    }

    @GetMapping("/hot")
    fun getRecentHotPosts() {
        println("getRecentHotPosts")
    }

//    @PatchMapping("/posts/{postId}")
//    fun updatePost(authentication: Authentication, @PathVariable postId: Long) {
//        println("updatePost")
//    }
//
//    @DeleteMapping("/posts/{postId}")
//    fun deletePost(authentication: Authentication, @PathVariable postId: Long) {
//        println("deletePost")
//    }

}