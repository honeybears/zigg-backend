package soma.achoom.zigg.feedback.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.feedback.entity.FeedbackRecipient
import soma.achoom.zigg.spaceuser.dto.SpaceUserResponseDto
import soma.achoom.zigg.spaceuser.entity.SpaceUser

import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FeedbackResponseDto(
    val feedbackId: UUID?,
    @Enumerated(EnumType.STRING)
    val feedbackType: FeedbackType?,
    val feedbackTimeline: String?,
    val feedbackMessage: String?,
    val creatorId: SpaceUserResponseDto?,
    val recipientId: MutableSet<SpaceUserResponseDto>?,
){

}