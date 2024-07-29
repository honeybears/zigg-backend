package soma.achoom.zigg.v0.dto.request

import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.dto.response.SpaceResponseDto
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser
import java.time.LocalDateTime

data class SpaceRequestDto(
    val spaceId:Long?,
    val spaceName: String?,

    val spaceImage: MultipartFile?,

    val spaceUsers: MutableSet<SpaceUserRequestDto>?,

    val comparisonVideo:MultipartFile?,

    val history: MutableSet<History>?,

    val tags: MutableSet<String>?,

    val createdAt: LocalDateTime?,
){


}
