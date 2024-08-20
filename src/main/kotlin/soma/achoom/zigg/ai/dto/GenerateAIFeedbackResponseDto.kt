package soma.achoom.zigg.ai.dto

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