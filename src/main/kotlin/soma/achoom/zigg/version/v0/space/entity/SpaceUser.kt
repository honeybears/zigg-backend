package soma.achoom.zigg.version.v0.space.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import soma.achoom.zigg.global.util.BaseEntity
import soma.achoom.zigg.version.v0.feedback.entity.FeedbackRecipient
import soma.achoom.zigg.version.v0.space.dto.SpaceUserResponseDto
import soma.achoom.zigg.version.v0.user.entity.User
import java.util.*

@Entity
@Table(name = "space_user")
data class SpaceUser(
    @Id
    var spaceUserId: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "space_id")
    @JsonBackReference
    var space: Space,

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var user: User,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var spaceRole: SpaceRole?,

    @OneToMany(mappedBy = "recipient", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonBackReference
    var feedbackRecipients: MutableSet<FeedbackRecipient> = mutableSetOf(),

    @Enumerated(EnumType.STRING)
    var inviteStatus: SpaceUserStatus,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,



    ) : BaseEntity() {
    @get:JsonInclude
    val userId: UUID
        get() = user.userId

    @get:JsonInclude
    val userName: String?
        get() = user.userName

    @get: JsonInclude
    val userNickname: String?
        get() = user.userNickname

    @get : JsonInclude
    val profileImageUrl: String
        get() = user.profileImageKey?: ""

    override fun hashCode(): Int {
        return Objects.hash(spaceUserId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val spaceUser = other as SpaceUser
        return spaceUserId == spaceUser.spaceUserId && space == spaceUser.space && user == spaceUser.user
    }

    fun toResponseDto() : SpaceUserResponseDto {
        return SpaceUserResponseDto(
            userNickname = this.user.userNickname,
            spaceUserId = this.spaceUserId,
            spaceRole = this.spaceRole,
            userName = this.user.userName
        )
    }
}

