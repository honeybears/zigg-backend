package soma.achoom.zigg.space.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.user.entity.User
import java.util.*

@Entity
//@EntityListeners(SpaceUserEntityListener::class)
@Table(name = "space_user")
class SpaceUser(
    @Id
    var spaceUserId: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "space_id")
    var space: Space?,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User?,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: SpaceRole = SpaceRole.USER,

    var withdraw : Boolean = false,

    ) : BaseEntity() {
}

