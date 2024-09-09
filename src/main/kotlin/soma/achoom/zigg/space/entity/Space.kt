package soma.achoom.zigg.space.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity

import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.invite.entity.Invite
import java.util.UUID

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "spaceId")
class Space(
    @Id
    var spaceId: UUID = UUID.randomUUID(),
    var spaceName: String,
    var spaceImageKey: String,
    var referenceVideoKey: String? = null,
    var referenceVideoUrl: String? = null,

    @OneToMany(mappedBy = "space", cascade = [CascadeType.ALL], orphanRemoval = true) // SpaceUser 엔티티와의 관계 설정
    var spaceUsers: MutableSet<SpaceUser> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true) // history 엔티티와의 관계 설정
    var histories: MutableSet<History> = mutableSetOf(),

    @OneToMany(mappedBy = "space", cascade = [CascadeType.ALL], orphanRemoval = true)
    var invites: MutableSet<Invite> = mutableSetOf(),


    ) : BaseEntity() {

}