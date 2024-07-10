package soma.achoom.zigg.v0.model

import jakarta.persistence.*

@Entity
data class Space(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val spaceId:Long,
    val spaceName: String,

    @OneToMany
    val user_in_space: MutableSet<User> = mutableSetOf(),
    
)
