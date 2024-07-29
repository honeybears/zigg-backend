package soma.achoom.zigg.v0.model

import jakarta.persistence.*

@Entity
@Table(name = "history")

data class History(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var historyId:Long?,
    var historyName: String?,
    @OneToMany
    var feedbacks: MutableSet<Feedback>?,

    var historyVideoUrl:String?


) :BaseEntity(){
}