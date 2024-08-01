package soma.achoom.zigg.v0.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.dto.request.HistoryRequestDto

@RestController
@RequestMapping("/api/v0/spaces/histories")
class HistoryController {
    @PostMapping("/{spaceId}")
    fun creatHistory(authentication: Authentication, @PathVariable spaceId: Long, historyRequestDto: HistoryRequestDto) {

    }
    @GetMapping("/{spaceId}/{historyId}")
    fun getHistory(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long) {

    }
    @PatchMapping("/{spaceId}/{historyId}")
    fun updateHistory(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long, historyRequestDto: HistoryRequestDto) {

    }
    @DeleteMapping("/{spaceId}/{historyId}")
    fun deleteHistory(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long) {

    }
}