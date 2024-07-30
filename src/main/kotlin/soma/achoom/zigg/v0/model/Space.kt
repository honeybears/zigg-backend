package soma.achoom.zigg.v0.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import java.util.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "spaceId")
data class Space(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var spaceId:Long?,
    var spaceName: String?,
    var spaceImageUrl:String?,

    var spaceThumbnailUrl:String?,

    var comparisonVideoUrl:String?,

    @OneToMany(mappedBy = "space",cascade = [CascadeType.ALL], orphanRemoval = true) // SpaceUser 엔티티와의 관계 설정
    var spaceUsers: MutableSet<SpaceUser> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true) // SpaceUser 엔티티와의 관계 설정
    var histories: MutableSet<History> = mutableSetOf()
):BaseEntity(){
    override fun hashCode(): Int {
        return Objects.hash(spaceName, spaceImageUrl, spaceId, comparisonVideoUrl, spaceThumbnailUrl)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}