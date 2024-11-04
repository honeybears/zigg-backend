package soma.achoom.zigg.feedback.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import soma.achoom.zigg.space.dto.SpaceUserResponseDto

import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FeedbackResponseDto(
    val feedbackId: Long?,
    @Enumerated(EnumType.STRING)
    val feedbackType: FeedbackType?,
    val feedbackTimeline: String?,
    val feedbackMessage: String?,
    val creatorId: SpaceUserResponseDto?,
    val recipientId: MutableSet<SpaceUserResponseDto>?,
){
    override fun toString(): String {
        return "FeedbackResponseDto(" +
                "feedbackId=$feedbackId, " +
                "feedbackType=$feedbackType, " +
                "feedbackTimeline=$feedbackTimeline, " +
                "feedbackMessage=$feedbackMessage, " +
                "creatorId=${creatorId?.toString()}, " +
                "recipientId=${recipientId?.map { it.toString() }}" +
                ")"
    }
}