package soma.achoom.zigg.version.v0.ai.dto

import soma.achoom.zigg.version.v0.ai.GeminiModelType
import java.util.UUID

data class GenerateAiFeedbackRequestDto(
    val bucketName : String,
    val historyId: UUID,
    val referenceVideoKey : String,
    val comparisonVideoKey: String,
    val modelName: GeminiModelType,
    val token:String
)