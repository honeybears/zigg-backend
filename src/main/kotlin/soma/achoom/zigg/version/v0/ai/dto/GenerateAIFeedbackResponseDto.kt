package soma.achoom.zigg.version.v0.ai.dto

import lombok.Data

data class GenerateAIFeedbackResponseDto(
    val timelineFeedbacks : List<AiFeedbackResponseDto>
) {
    @Data
    class AiFeedbackResponseDto(
        val time:String,
        val message:String
    )
}