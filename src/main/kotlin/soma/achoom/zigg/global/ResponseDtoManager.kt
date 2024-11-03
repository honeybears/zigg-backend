package soma.achoom.zigg.global

import org.springframework.stereotype.Component
import soma.achoom.zigg.comment.dto.CommentResponseDto
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.content.dto.VideoResponseDto
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.global.dto.PageInfo
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.invite.dto.InviteResponseDto
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.post.dto.PostResponseDto
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.dto.SpaceUserResponseDto
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.entity.User

@Deprecated("Create ResponseDto self")
class ResponseDtoManager(
    private val s3Service: S3Service
) {

    fun generateHistoryResponseShortDto(history: History): HistoryResponseDto {
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.name,
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoKey.videoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoThumbnailUrl.imageKey),
            createdAt = history.createAt,
            feedbacks = null,
            videoDuration = history.videoKey.duration,
            feedbackCount = history.feedbacks.size
        )
    }

    fun generateHistoryResponseDto(history: History): HistoryResponseDto {
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.name,
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoKey.videoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoThumbnailUrl.imageKey),
            createdAt = history.createAt,
            feedbacks = history.feedbacks.map { generateFeedbackResponseDto(it) }.toMutableSet(),
            videoDuration = history.videoKey.duration,
            feedbackCount = history.feedbacks.size
        )
    }

    fun generateFeedbackResponseDto(feedback: Feedback): FeedbackResponseDto {
        return FeedbackResponseDto(
            feedbackId = feedback.feedbackId,
            feedbackTimeline = feedback.timeline,
            feedbackType = feedback.type,
            feedbackMessage = feedback.message,
            creatorId = generateSpaceUserResponseDto(feedback.creator),
            recipientId = feedback.recipients.map { generateSpaceUserResponseDto(it) }.toMutableSet()
        )
    }

    fun generateSpaceUserResponseDto(spaceUser: SpaceUser): SpaceUserResponseDto {
        return SpaceUserResponseDto(
            userId = spaceUser.user?.userId,
            spaceUserId = spaceUser.spaceUserId,
            userName = spaceUser.user?.name ?: "알수없음",
            userNickname = spaceUser.user?.nickname ?: "알수없음",
            spaceRole = spaceUser.role,
            profileImageUrl = s3Service.getPreSignedGetUrl(spaceUser.user?.profileImageKey?.imageKey)
        )
    }

    fun generateUserResponseDto(user: User): UserResponseDto {
        return UserResponseDto(
            userId = user.userId,
            userName = user.name,
            userNickname = user.nickname,
            profileImageUrl = s3Service.getPreSignedGetUrl(user.profileImageKey.imageKey),
            profileBannerImageUrl = user.profileBannerImageKey?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
            userTags = user.tags,
            userDescription = user.description,
            createdAt = user.createAt
        )
    }
}