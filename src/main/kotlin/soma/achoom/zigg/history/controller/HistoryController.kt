package soma.achoom.zigg.history.controller

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
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.history.dto.HistoryRequestDto
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import soma.achoom.zigg.history.service.HistoryService
import soma.achoom.zigg.s3.service.S3DataType
import soma.achoom.zigg.s3.service.S3Service
import java.util.UUID

@RestController
@RequestMapping("/api/v0/spaces/histories")
class HistoryController @Autowired constructor(
    private val historyService: HistoryService,
    private val s3Service: S3Service,
    private val responseDtoManager: ResponseDtoManager

) {
    @PostMapping("/pre-signed-url/{value}")
    fun getPreSignedUrl(
        @RequestBody uploadContentTypeRequestDto: UploadContentTypeRequestDto,
        @PathVariable value: String
    ): ResponseEntity<String> {
        if (value.trim() == "video") {
            val preSignedUrl =
                s3Service.getPreSignedPutUrl(S3DataType.HISTORY_VIDEO, UUID.randomUUID(), uploadContentTypeRequestDto)
            return ResponseEntity.ok(preSignedUrl)

        } else if (value.trim() == "thumbnail") {
            val preSignedUrl = s3Service.getPreSignedPutUrl(
                S3DataType.HISTORY_THUMBNAIL,
                UUID.randomUUID(),
                uploadContentTypeRequestDto
            )
            return ResponseEntity.ok(preSignedUrl)
        } else
            return ResponseEntity.badRequest().build()
    }


    @GetMapping("/{spaceId}")
    fun getHistories(
        authentication: Authentication,
        @PathVariable spaceId: UUID
    ): ResponseEntity<List<HistoryResponseDto>> {
        val histories = historyService.getHistories(authentication, spaceId)
        return ResponseEntity.ok(
            histories.map {
                responseDtoManager.generateHistoryResponseShortDto(it)
            }.toList()
        )
    }

    @PostMapping("/{spaceId}")
    fun creatHistory(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @RequestBody historyRequestDto: HistoryRequestDto
    ): ResponseEntity<HistoryResponseDto> {
        val history = historyService.createHistory(authentication, spaceId, historyRequestDto)
        return ResponseEntity.ok(responseDtoManager.generateHistoryResponseShortDto(history))
    }

    @GetMapping("/{spaceId}/{historyId}")
    fun getHistory(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @PathVariable historyId: UUID
    ): ResponseEntity<HistoryResponseDto> {
        val history = historyService.getHistory(authentication, spaceId, historyId)
        return ResponseEntity.ok(responseDtoManager.generateHistoryResponseDto(history))
    }

    @PatchMapping("/{spaceId}/{historyId}")
    fun updateHistory(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @PathVariable historyId: UUID,
        @RequestBody historyRequestDto: HistoryRequestDto
    ): ResponseEntity<HistoryResponseDto> {
        val history = historyService.updateHistory(authentication, spaceId, historyId, historyRequestDto)
        return ResponseEntity.ok( responseDtoManager.generateHistoryResponseDto(history))
    }

    @DeleteMapping("/{spaceId}/{historyId}")
    fun deleteHistory(
        authentication: Authentication,
        @PathVariable spaceId: UUID,
        @PathVariable historyId: UUID
    ): ResponseEntity<Void> {
        historyService.deleteHistory(authentication, spaceId, historyId)
        return ResponseEntity.noContent().build()
    }
}