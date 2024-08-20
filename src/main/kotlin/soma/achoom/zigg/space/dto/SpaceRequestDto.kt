package soma.achoom.zigg.space.dto
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime
import java.util.UUID

data class SpaceRequestDto(
    val spaceId: UUID?,
    @NotBlank(message = "space 이름은 필수입니다.")
    @Max(value = 20, message = "space 이름은 20자 이하여야 합니다.")
    @Min(value = 2, message = "space 이름은 2자 이상이어야 합니다.")
    val spaceName: String,
    val spaceUsers: List<SpaceUserRequestDto>,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
) {

}
