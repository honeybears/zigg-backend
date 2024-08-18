package soma.achoom.zigg.v0.ai

import com.fasterxml.jackson.annotation.JsonValue

enum class GeminiModelType(@JsonValue val modelName:String) {
    PRO("gemini-1.5-pro"),
    FLASH("gemini-1.5-flash"),
}