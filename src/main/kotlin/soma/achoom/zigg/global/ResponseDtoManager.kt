package soma.achoom.zigg.global

import org.springframework.stereotype.Component
import soma.achoom.zigg.comment.dto.CommentResponseDto
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.content.dto.VideoRequestDto
import soma.achoom.zigg.content.dto.VideoResponseDto
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.global.dto.PageInfo
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.invite.dto.InviteResponseDto
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.post.dto.PostListResponseDto
import soma.achoom.zigg.post.dto.PostRequestDto
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
    fun generateSpaceResponseDto(space: Space): SpaceResponseDto{
        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.spaceImageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = space.spaceUsers.filter{it.withdraw.not()}.map {
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
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.spaceImageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = space.spaceUsers.filter{it.withdraw.not()}.map {
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
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.historyVideoKey.videoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.historyVideoThumbnailUrl.imageKey),
            createdAt = history.createAt,
            feedbacks = null,
            videoDuration = history.historyVideoKey.videoDuration,
            feedbackCount = history.feedbacks.size
        )
    }
    fun generateHistoryResponseDto(history: History) : HistoryResponseDto{
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.historyName,
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.historyVideoKey.videoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.historyVideoThumbnailUrl.imageKey),
            createdAt = history.createAt,
            feedbacks = history.feedbacks.map { generateFeedbackResponseDto(it) }.toMutableSet(),
            videoDuration = history.historyVideoKey.videoDuration,
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
            userName = spaceUser.user?.userName ?: "알수없음",
            userNickname = spaceUser.user?.userNickname ?: "알수없음",
            spaceRole = spaceUser.spaceRole,
            profileImageUrl = s3Service.getPreSignedGetUrl(spaceUser.user?.profileImageKey?.imageKey)
        )
    }
    fun generateUserResponseDto(user: User): UserResponseDto {
        return UserResponseDto(
            userId = user.userId,
            userName = user.userName,
            userNickname = user.userNickname,
            profileImageUrl = s3Service.getPreSignedGetUrl(user.profileImageKey.imageKey),
            profileBannerImageUrl = user.profileBannerImageKey?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
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
                spaceImageUrl = s3Service.getPreSignedGetUrl(invite.space.spaceImageKey.imageKey),
                referenceVideoUrl = invite.space.referenceVideoKey,
                spaceUsers = invite.space.spaceUsers.filter{it.withdraw.not()}.map {
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
    fun generatePostListResponseDto(posts: List<Post>,pageInfo: PageInfo): PostListResponseDto {
        return PostListResponseDto(
            posts = posts.map { generatePostShortResponseDto(it) }.toMutableList(),
            pageInfo = pageInfo
        )
    }
    fun generatePostResponseDto(post : Post): PostResponseDto {
        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.postTitle,
            postMessage = post.postMessage,
            postImageContent = post.postImageContent.map { s3Service.getPreSignedGetUrl(it.imageKey) }.toList(),
            postVideoContent = post.postVideoContent.map {
                VideoResponseDto(
                    videoUrl = s3Service.getPreSignedGetUrl(it.videoKey),
                    videoDuration = it.videoDuration
                )
            }.toList(),
            comments = post.postComments.map { generateCommentResponseDto(it) }.toMutableList()
        )
    }
    fun generatePostShortResponseDto(post : Post): PostResponseDto {
        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.postTitle,
        )
    }

    fun generateCommentResponseDto(comment: Comment): CommentResponseDto {
        return CommentResponseDto(
            commentId = comment.commentId!!,
            commentMessage = comment.commentMessage,
            commentCreator = generateUserResponseDto(comment.commentCreator),
            commentLike = comment.commentLike
        )
    }


}