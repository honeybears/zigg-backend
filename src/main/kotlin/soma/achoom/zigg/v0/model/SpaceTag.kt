package soma.achoom.zigg.v0.model

import jakarta.persistence.*

@Entity
data class SpaceTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val spaceTagId:Long,


    @ManyToOne
    @JoinColumn(name = "spaceId")
    val space: Space,

    val tag: String
) {

}