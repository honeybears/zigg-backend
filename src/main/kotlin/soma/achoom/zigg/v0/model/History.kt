package soma.achoom.zigg.v0.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
data class History(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var historyId: Long?,

    var historyName: String?,

    @ManyToOne
    @JoinColumn(name = "space_id")
    @JsonBackReference
    var space: Space,

    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    var feedbacks: MutableSet<Feedback> = mutableSetOf(),


    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

) : BaseEntity() {

        override fun hashCode(): Int {
            return historyId.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is History) return false
            return historyId == other.historyId && historyName == other.historyName && space == other.space && feedbacks == other.feedbacks && isDeleted == other.isDeleted
        }
}