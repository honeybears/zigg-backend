package soma.achoom.zigg.v0.model

import jakarta.persistence.*

@Entity
@Table(name = "history")

data class History(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long?,

    @OneToMany
    val feedbacks: MutableSet<Feedback> = mutableSetOf(),

) :BaseEntity(){
}