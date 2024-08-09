package soma.achoom.zigg.v0.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "spaceId")
data class Space(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var spaceId: Long?,
    var spaceName: String?,
    var spaceImageUrl: String? = null,
    @OneToMany(mappedBy = "space", cascade = [CascadeType.ALL], orphanRemoval = true) // SpaceUser 엔티티와의 관계 설정
    var spaceUsers: MutableSet<SpaceUser> = mutableSetOf(),

    @OneToMany(mappedBy = "space",cascade = [CascadeType.ALL], orphanRemoval = true) // history 엔티티와의 관계 설정
    var histories: MutableSet<History> = mutableSetOf(),

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false


) : BaseEntity() {

}