package soma.achoom.zigg.v0.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import kotlin.time.Duration

@Entity
@Table(name = "feedback")
data class Feedback(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val feedbackId:Long?,

    @NotNull
    @Enumerated(EnumType.STRING)
    val feedbackType: FeedbackType,

    val feedbackTimeline:Duration,

    val feedbackMessage: String,

    @OneToOne
    @Nullable
    val feedbackCreator: User?,

    @OneToMany
    val recipients:MutableSet<User>

) : BaseEntity(){
}