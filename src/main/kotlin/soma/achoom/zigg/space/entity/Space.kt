package soma.achoom.zigg.space.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.global.BaseEntity

import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.invite.entity.Invite
import java.time.LocalDateTime
import java.util.UUID

@EntityListeners(SpaceEntityListener::class)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "spaceId")
class Space(
    @Id
    var spaceId: UUID = UUID.randomUUID(),
    var name: String,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    var imageKey: Image,

    var referenceVideoKey: String? = null,

    @OneToMany(mappedBy = "space", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var users: MutableSet<SpaceUser> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var histories: MutableSet<History> = mutableSetOf(),

    @OneToMany(mappedBy = "space", cascade = [CascadeType.ALL], orphanRemoval = true)
    var invites: MutableSet<Invite> = mutableSetOf(),

    ) : BaseEntity() {

    fun addHistory(history: History) {
        histories.add(history)
        this.updateAt = LocalDateTime.now()
    }

}