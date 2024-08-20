package soma.achoom.zigg.version.v0.history.dto

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull


data class HistoryRequestDto(
    val historyName: String?,
    @NotNull
    @NotBlank
    val historyVideoUrl: String,
    var videoDuration: String

){

}