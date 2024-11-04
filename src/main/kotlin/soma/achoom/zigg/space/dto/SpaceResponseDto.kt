package soma.achoom.zigg.space.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.invite.dto.InviteResponseDto

import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SpaceResponseDto(
    val spaceId: Long?,
    val spaceName: String?,
    val spaceImageUrl: String?,
    val spaceUsers: MutableSet<SpaceUserResponseDto>?,
    val history: MutableSet<HistoryResponseDto>? = null,
    val referenceVideoUrl:String? = null,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val invites : MutableSet<InviteResponseDto>? = null
) {
    override fun toString(): String {
        return "SpaceResponseDto(\n" +
                "spaceId=$spaceId,\n" +
                "spaceName=$spaceName,\n" +
                "spaceImageUrl=$spaceImageUrl,\n" +
                "spaceUsers=${spaceUsers?.map { it.toString()}},\n" +
                "history=${history?.map { it.toString() + "\n"}},\n" +
                "referenceVideoUrl=$referenceVideoUrl,\n" +
                "invites=${invites?.map { it.toString() + "\n"}},\n" +
                "createdAt=$createdAt,\n" +
                "updatedAt=$updatedAt\n" +
                ")\n"
    }
}
