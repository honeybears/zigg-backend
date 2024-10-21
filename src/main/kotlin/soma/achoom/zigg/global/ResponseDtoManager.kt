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

@Component
class ResponseDtoManager(
    private val s3Service: S3Service
) {
    fun generateSpaceResponseDto(space: Space): SpaceResponseDto {
        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.name,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.imageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = space.users.filter { it.withdraw.not() }.map {
                generateSpaceUserResponseDto(it)
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
            history = space.histories.map { generateHistoryResponseDto(it) }.toMutableSet(),
            invites = space.invites.map { generateInviteShortResponseDto(it) }.toMutableSet()
        )
    }

    fun generateSpaceResponseShortDto(space: Space): SpaceResponseDto {
        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.name,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.imageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = space.users.filter { it.withdraw.not() }.map {
                generateSpaceUserResponseDto(it)
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
            history = space.histories.map { generateHistoryResponseShortDto(it) }.toMutableSet(),
            invites = space.invites.map { generateInviteShortResponseDto(it) }.toMutableSet()
        )
    }

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

    fun generateInviteResponseDto(invite: Invite): InviteResponseDto {
        return InviteResponseDto(
            inviteId = invite.inviteId,
            invitedUser = generateUserResponseDto(invite.invitee),
            inviter = generateUserResponseDto(invite.inviter),
            space = SpaceResponseDto(
                spaceId = invite.space.spaceId,
                spaceName = invite.space.name,
                spaceImageUrl = s3Service.getPreSignedGetUrl(invite.space.imageKey.imageKey),
                referenceVideoUrl = invite.space.referenceVideoKey,
                spaceUsers = invite.space.users.filter { it.withdraw.not() }.map {
                    generateSpaceUserResponseDto(it)
                }.toMutableSet(),
                createdAt = invite.space.createAt,
                updatedAt = invite.space.updateAt,
            ),
            createdAt = invite.createAt,

            )
    }

    fun generateInviteShortResponseDto(invite: Invite): InviteResponseDto {
        return InviteResponseDto(
            inviteId = invite.inviteId,
            invitedUser = generateUserResponseDto(invite.invitee),
            inviter = generateUserResponseDto(invite.inviter),
            createdAt = invite.createAt
        )
    }


    fun generatePostResponseDto(post: Post): PostResponseDto {
        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.title,
            postMessage = post.textContent,
            postImageContents = post.imageContents.map { s3Service.getPreSignedGetUrl(it.imageKey) }.toList(),
            postVideoContent = post.videoContent?.let {
                VideoResponseDto(
                    videoUrl = s3Service.getPreSignedGetUrl(post.videoContent!!.videoKey),
                    videoDuration = it.duration
                )
            },
            postThumbnailImage = post.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
            comments = post.comments.map { generateCommentResponseDto(it) }.toMutableList()
        )
    }

    fun generatePostShortResponseDto(post: Post): PostResponseDto {
        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.title,
            postMessage = post.textContent,
            postImageContents = post.imageContents.first().imageKey.let { s3Service.getPreSignedGetUrl(it) }.let { listOf(it) },
            postThumbnailImage = post.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
        )
    }

    fun generateCommentResponseDto(comment: Comment): CommentResponseDto {
        return CommentResponseDto(
            commentId = comment.commentId!!,
            commentMessage = comment.textComment,
            commentCreator = if(comment.isDeleted) UserResponseDto(
                userName = "알 수 없음",
                userNickname = "알 수 없음",
                profileImageUrl = null,
                profileBannerImageUrl = null,
                userTags = null,
                userDescription = null,
                createdAt = null
            ) else generateUserResponseDto(comment.creator),
            commentLike = comment.likes
        )
    }


}