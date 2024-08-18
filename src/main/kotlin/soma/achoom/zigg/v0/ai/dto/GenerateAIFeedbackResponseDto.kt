package soma.achoom.zigg.v0.ai.dto

import lombok.Data
import java.util.UUID

data class GenerateAIFeedbackResponseDto(
    val historyId : UUID,
    val timelineFeedback : List<AiFeedbackResponseDto>
) {
    @Data
    class AiFeedbackResponseDto(
        val time:String,
        val message:String
    )
}