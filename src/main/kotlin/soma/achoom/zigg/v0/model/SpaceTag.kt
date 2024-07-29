package soma.achoom.zigg.v0.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
data class SpaceTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var spaceTagId:Long?,


    @ManyToOne
    @JoinColumn(name = "spaceId")
    @JsonBackReference

    var space: Space,

    var tag: String
) {

}