package soma.achoom.zigg.space.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.feedback.entity.FeedbackRecipient
import soma.achoom.zigg.user.entity.User
import java.util.*

@Entity
@Table(name = "space_user")
class SpaceUser(
    @Id
    var spaceUserId: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "space_id")
    @JsonBackReference
    var space: Space,

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var user: User?,

    var userNickname: String = user?.userNickname!!,

    var userName: String = user?.userName!!,

    var profileImageUrl: String = user?.profileImageKey ?: "",

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var spaceRole: SpaceRole?,

    @OneToMany(mappedBy = "recipient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    var feedbackRecipients: MutableSet<FeedbackRecipient> = mutableSetOf(),

    @OneToMany(mappedBy = "feedbackCreator", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    var feedbackCreator: MutableSet<Feedback> = mutableSetOf(),
    ) : BaseEntity() {

    @get:JsonInclude
    val userId: UUID?
        get() = user?.userId

}

