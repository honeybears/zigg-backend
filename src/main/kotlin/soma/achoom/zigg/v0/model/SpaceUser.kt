package soma.achoom.zigg.v0.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import soma.achoom.zigg.v0.model.enums.SpaceRole
import soma.achoom.zigg.v0.model.enums.SpaceUserStatus
import java.util.*

@Entity
@Table(name = "space_user")
data class SpaceUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var spaceUserId: Long?,

    @ManyToOne
    @JoinColumn(name = "space_id")
    @JsonBackReference

    var space: Space,

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var user: User,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var spaceRole: SpaceRole?,

    @Enumerated(EnumType.STRING)
    var inviteStatus: SpaceUserStatus,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

    ) : BaseEntity() {
    @get:JsonInclude
    val userId: Long
        get() = user.userId!!

    @get:JsonInclude
    val userName: String?
        get() = user.userName

    @get: JsonInclude
    val userNickname: String?
        get() = user.userNickname

    @get : JsonInclude
    val profileImageUrl: String
        get() = user.profileImageUrl

    override fun hashCode(): Int {
        return Objects.hash(user, inviteStatus, spaceRole, spaceUserId)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}

