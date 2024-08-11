package soma.achoom.zigg.v0.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.dto.request.HistoryRequestDto
import soma.achoom.zigg.v0.dto.response.HistoryResponseDto
import soma.achoom.zigg.v0.model.enums.GCSDataType
import soma.achoom.zigg.v0.service.GCSService
import soma.achoom.zigg.v0.service.HistoryService
import java.util.UUID

@RestController
@RequestMapping("/api/v0/spaces/histories")
class HistoryController @Autowired constructor(
    private val historyService: HistoryService,
    private val gcsService: GCSService

) {

    @GetMapping("/pre-signed-url")
    fun getPreSignedUrl() : ResponseEntity<String> {
        val preSignedUrl = gcsService.getPreSignedPutUrl(GCSDataType.HISTORY_VIDEO,UUID.randomUUID())
        return ResponseEntity.ok(preSignedUrl)
    }

    @PostMapping("/{spaceId}")
    fun creatHistory(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @RequestBody historyRequestDto: HistoryRequestDto
    ) :ResponseEntity<HistoryResponseDto> {
        val historyResponseDto = historyService.createHistory(authentication, spaceId,historyRequestDto)
        return ResponseEntity.ok(historyResponseDto)
    }
    @GetMapping("/{spaceId}/{historyId}")
    fun getHistory(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long) {

    }
    @PatchMapping("/{spaceId}/{historyId}")
    fun updateHistory(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long, @RequestBody historyRequestDto: HistoryRequestDto) {

    }
    @DeleteMapping("/{spaceId}/{historyId}")
    fun deleteHistory(authentication: Authentication, @PathVariable spaceId: Long, @PathVariable historyId: Long) {

    }
}