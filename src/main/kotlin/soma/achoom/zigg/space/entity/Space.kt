package soma.achoom.zigg.space.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import soma.achoom.zigg.global.util.BaseEntity

import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.user.entity.User
import java.util.UUID

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "spaceId")
data class Space(
    @Id
    var spaceId: UUID = UUID.randomUUID(),
    var spaceName: String?,
    var spaceImageKey: String,

    var referenceVideoKey: String? = null,

    var referenceVideoUrl: String? = null,

    @OneToMany(mappedBy = "space", cascade = [CascadeType.ALL], orphanRemoval = true) // SpaceUser 엔티티와의 관계 설정
    var spaceUsers: MutableSet<soma.achoom.zigg.space.entity.SpaceUser> = mutableSetOf(),

    @OneToMany(mappedBy = "space",cascade = [CascadeType.ALL], orphanRemoval = true) // history 엔티티와의 관계 설정
    var histories: MutableSet<History> = mutableSetOf(),

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false


) : BaseEntity() {
    companion object{
        fun createSpace(spaceName:String, spaceImageUrl: String, users:MutableSet<User>, admin: User): soma.achoom.zigg.space.entity.Space {
            val space = soma.achoom.zigg.space.entity.Space(
                spaceName = spaceName,
                spaceImageKey = spaceImageUrl,
            )
            space.spaceUsers.add(
                soma.achoom.zigg.space.entity.SpaceUser(
                    space = space,
                    user = admin,
                    inviteStatus = soma.achoom.zigg.space.entity.SpaceUserStatus.ACCEPTED,
                    spaceRole = soma.achoom.zigg.space.entity.SpaceRole.ADMIN
                )
            )
            users.forEach {
                space.spaceUsers.add(
                    soma.achoom.zigg.space.entity.SpaceUser(
                        space = space,
                        user = it,
                        inviteStatus = soma.achoom.zigg.space.entity.SpaceUserStatus.ACCEPTED,
                        spaceRole = soma.achoom.zigg.space.entity.SpaceRole.USER
                    )
                )
            }
            return space
        }
    }
}