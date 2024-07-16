package soma.achoom.zigg.v0.model

import jakarta.persistence.*

@Entity
data class Space(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val spaceId:Long,
    val spaceName: String,

    val comparisonVideo:Boolean,

    @OneToMany(mappedBy = "space") // SpaceUser 엔티티와의 관계 설정
    val spaceUsers: MutableSet<SpaceUser> = mutableSetOf(),

    @OneToMany
    val history: MutableSet<History> = mutableSetOf(),
):BaseEntity()
