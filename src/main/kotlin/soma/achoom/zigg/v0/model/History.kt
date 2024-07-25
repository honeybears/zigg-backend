package soma.achoom.zigg.v0.model

import jakarta.persistence.*

@Entity
@Table(name = "history")

data class History(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val historyId:Long?,
    val historyName: String,
    @OneToMany
    val feedbacks: MutableSet<Feedback>?,

    val historyVideoUrl:String

) :BaseEntity(){
}