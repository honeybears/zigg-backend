package soma.achoom.zigg.history.dto

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull


data class HistoryRequestDto(
    val historyName: String?,
    @NotNull
    @NotBlank
    val historyVideoUrl: String,
    val historyThumbnailUrl:String,
    var videoDuration: String

){

}