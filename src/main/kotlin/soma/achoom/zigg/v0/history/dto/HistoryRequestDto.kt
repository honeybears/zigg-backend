package soma.achoom.zigg.v0.history.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import soma.achoom.zigg.v0.history.entity.History
import soma.achoom.zigg.v0.space.entity.Space
import java.util.UUID

data class HistoryRequestDto(
    val historyName: String?,
    @NotNull
    @NotBlank
    val historyVideoUrl: String?
){

}