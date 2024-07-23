package soma.achoom.zigg.v0.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import kotlin.time.Duration

@Entity
@Table(name = "feedback")
data class Feedback(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long?,

    @NotNull
    @Enumerated(EnumType.STRING)
    val type:FeedbackType,

    val timeline:Duration,

    val feedbackMessage: String,

    @OneToOne
    @Nullable
    val creatorId:User?,

    @OneToMany
    val recipientId:MutableSet<User> = mutableSetOf(),

) :BaseEntity(){
}