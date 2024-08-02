package soma.achoom.zigg.v0.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.v0.auth.OAuthProviderEnum
import soma.achoom.zigg.v0.model.enums.UserRole
import java.util.*


@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,

    var userName: String? = null,

    var userNickname: String? = null,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    var profileImageUrl: String = "default",

    @Enumerated(EnumType.STRING)
    var platform: OAuthProviderEnum,

    @JsonBackReference
    var providerId: String,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

) : BaseEntity(){
    override fun equals(other: Any?): Boolean {
        return providerId == (other as User).providerId

    }
    override fun hashCode(): Int {
        return Objects.hash(userId)
    }

}