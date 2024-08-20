package soma.achoom.zigg.version.v0.space.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.version.v0.history.dto.HistoryResponseDto

import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SpaceResponseDto(
    val spaceId: UUID?,
    val spaceName: String?,
    val spaceImageUrl: String?,
    val spaceUsers: MutableSet<SpaceUserResponseDto>?,
    val history: MutableSet<HistoryResponseDto>? = null,
    val referenceVideoUrl:String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {

}
