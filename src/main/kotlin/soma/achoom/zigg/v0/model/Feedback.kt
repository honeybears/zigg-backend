package soma.achoom.zigg.v0.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

@Entity
data class Feedback(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var feedbackId: Long?,

    @NotNull
    @Enumerated(EnumType.STRING)
    var feedbackType: FeedbackType = FeedbackType.USER,

    var feedbackTimeline: String?,

    var feedbackMessage: String?,

    @OneToOne
    @Nullable
    var feedbackCreator: SpaceUser?,

    @OneToMany
    var recipients: MutableSet<SpaceUser>?,

    @ManyToOne
    @JoinColumn(name = "history_id")
    var history: History,  // Ensure this property exists

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

) : BaseEntity() {
}