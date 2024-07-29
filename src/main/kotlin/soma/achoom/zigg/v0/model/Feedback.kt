package soma.achoom.zigg.v0.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import kotlin.time.Duration

@Entity
@Table(name = "feedback")
data class Feedback(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var feedbackId:Long?,

    @NotNull
    @Enumerated(EnumType.STRING)
    var feedbackType: FeedbackType,

    var feedbackTimeline:Duration,

    var feedbackMessage: String?,

    @OneToOne
    @Nullable
    var feedbackCreator: User?,

    @OneToMany
    var recipients:MutableSet<User>

) : BaseEntity(){
}