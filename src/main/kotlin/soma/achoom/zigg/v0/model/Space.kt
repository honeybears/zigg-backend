package soma.achoom.zigg.v0.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import soma.achoom.zigg.v0.model.enums.SpaceRole
import soma.achoom.zigg.v0.model.enums.SpaceUserStatus
import java.util.UUID

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "spaceId")
data class Space(
    @Id
    var spaceId: UUID = UUID.randomUUID(),
    var spaceName: String?,
    var spaceImageUrl: String,
    @OneToMany(mappedBy = "space", cascade = [CascadeType.ALL], orphanRemoval = true) // SpaceUser 엔티티와의 관계 설정
    var spaceUsers: MutableSet<SpaceUser> = mutableSetOf(),

    @OneToMany(mappedBy = "space",cascade = [CascadeType.ALL], orphanRemoval = true) // history 엔티티와의 관계 설정
    var histories: MutableSet<History> = mutableSetOf(),

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false


) : BaseEntity() {
    companion object{
        fun createSpace(spaceName:String,spaceImageUrl: String,users:MutableSet<User>,admin:User):Space{
            val space = Space(
                spaceName = spaceName,
                spaceImageUrl = spaceImageUrl,
            )
            space.spaceUsers.add(
                SpaceUser(
                    space = space,
                    user = admin,
                    inviteStatus = SpaceUserStatus.ACCEPTED,
                    spaceRole = SpaceRole.ADMIN
                )
            )
            users.forEach {
                space.spaceUsers.add(
                    SpaceUser(
                        space = space,
                        user = it,
                        inviteStatus = SpaceUserStatus.ACCEPTED,
                        spaceRole = SpaceRole.USER
                    )
                )
            }
            return space
        }
    }
}