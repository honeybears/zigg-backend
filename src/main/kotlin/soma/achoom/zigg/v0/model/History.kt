package soma.achoom.zigg.v0.model

import jakarta.persistence.*

@Entity
data class History(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var historyId: Long?,

    var historyName: String?,

    @ManyToOne
    @JoinColumn(name = "space_id")
    var space: Space,

    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    var feedbacks: MutableSet<Feedback> = mutableSetOf(),

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

) : BaseEntity() {
}