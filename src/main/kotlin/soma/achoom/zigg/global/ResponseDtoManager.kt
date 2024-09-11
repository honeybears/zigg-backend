package soma.achoom.zigg.global

import org.springframework.stereotype.Component
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.invite.dto.InviteResponseDto
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.dto.SpaceUserResponseDto
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.entity.User

@Component
class ResponseDtoManager(
    private val s3Service: S3Service
) {
    fun generateSpaceResponseDto(space: Space): SpaceResponseDto{
        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.spaceImageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = space.spaceUsers.map {
                generateSpaceUserResponseDto(it)
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
            history = space.histories.map { generateHistoryResponseDto(it) }.toMutableSet(),
            invites = space.invites.map { generateInviteShortResponseDto(it) }.toMutableSet()
        )
    }
    fun generateSpaceResponseShortDto(space: Space): SpaceResponseDto{
        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.spaceImageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = space.spaceUsers.map {
                generateSpaceUserResponseDto(it)
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
            history = space.histories.map { generateHistoryResponseShortDto(it) }.toMutableSet(),
            invites = space.invites.map { generateInviteShortResponseDto(it) }.toMutableSet()
        )
    }
    fun generateHistoryResponseShortDto(history: History) : HistoryResponseDto{
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.historyName,
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.historyVideoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.historyVideoThumbnailUrl),
            createdAt = history.createAt,
            feedbacks = null,
            videoDuration = history.videoDuration,
            feedbackCount = history.feedbacks.size
        )
    }
    fun generateHistoryResponseDto(history: History) : HistoryResponseDto{
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.historyName,
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.historyVideoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.historyVideoThumbnailUrl),
            createdAt = history.createAt,
            feedbacks = history.feedbacks.map { generateFeedbackResponseDto(it) }.toMutableSet(),
            videoDuration = history.videoDuration,
            feedbackCount = history.feedbacks.size
        )
    }
    fun generateFeedbackResponseDto(feedback:Feedback) : FeedbackResponseDto{
        return FeedbackResponseDto(
            feedbackId = feedback.feedbackId,
            feedbackTimeline = feedback.feedbackTimeline,
            feedbackType = feedback.feedbackType,
            feedbackMessage = feedback.feedbackMessage,
            creatorId = generateSpaceUserResponseDto(feedback.feedbackCreator),
            recipientId = feedback.recipients.map { generateSpaceUserResponseDto(it) }.toMutableSet()
        )
    }
    fun generateSpaceUserResponseDto(spaceUser: SpaceUser): SpaceUserResponseDto {
        return SpaceUserResponseDto(
            spaceUserId = spaceUser.spaceUserId,
            userName = spaceUser.user?.userName,
            userNickname = spaceUser.user?.userNickname,
            spaceRole = spaceUser.spaceRole,
            profileImageUrl = s3Service.getPreSignedGetUrl(spaceUser.user?.profileImageKey)
        )
    }
    fun generateUserResponseDto(user: User): UserResponseDto {
        return UserResponseDto(
            userId = user.userId,
            userName = user.userName,
            userNickname = user.userNickname,
            profileImageUrl = s3Service.getPreSignedGetUrl(user.profileImageKey),
            profileBannerImageUrl = user.profileBannerImageKey?.let { s3Service.getPreSignedGetUrl(it) },
        )
    }
    fun generateInviteResponseDto(invite: Invite): InviteResponseDto {
        return InviteResponseDto(
            inviteId = invite.inviteId,
            invitedUser = generateUserResponseDto(invite.invitee),
            inviter = generateUserResponseDto(invite.inviter),
            space = SpaceResponseDto(
                spaceId = invite.space.spaceId,
                spaceName = invite.space.spaceName,
                spaceImageUrl = s3Service.getPreSignedGetUrl(invite.space.spaceImageKey),
                referenceVideoUrl = invite.space.referenceVideoKey,
                spaceUsers = invite.space.spaceUsers.map {
                    generateSpaceUserResponseDto(it)
                }.toMutableSet(),
                createdAt = invite.space.createAt,
                updatedAt = invite.space.updateAt,
            ),
            createdAt = invite.createAt!!,

        )
    }
    fun generateInviteShortResponseDto(invite:Invite): InviteResponseDto{
        return InviteResponseDto(
            inviteId = invite.inviteId,
            invitedUser = generateUserResponseDto(invite.invitee),
            inviter = generateUserResponseDto(invite.inviter),
            createdAt = invite.createAt!!,
            )
    }

}