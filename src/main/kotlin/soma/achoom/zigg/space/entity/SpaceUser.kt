package soma.achoom.zigg.space.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User
import java.util.*

@Entity
@Table(name = "space_user")
class SpaceUser(
    @Id
    var spaceUserId: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "space_id")
    var space: Space,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User?,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var spaceRole: SpaceRole?,

    @ManyToMany(mappedBy = "recipients", fetch = FetchType.LAZY)
    var feedbackRecipients: MutableList<Feedback> = mutableListOf(),

    @OneToMany(mappedBy = "feedbackCreator", orphanRemoval = true, fetch = FetchType.LAZY)

    var feedbackCreator: MutableSet<Feedback> = mutableSetOf(),
    ) : BaseEntity() {

    @get:JsonInclude
    val userId: UUID?
        get() = user?.userId

}

