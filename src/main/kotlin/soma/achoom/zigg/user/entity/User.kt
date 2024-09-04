package soma.achoom.zigg.user.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.firebase.entity.FCMToken
import java.util.*


@Entity
@Table(name = "`user`")
data class User(
    @Id
    var userId: UUID = UUID.randomUUID(),

    var userName: String? = null,

    var userNickname: String? = null,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    var profileImageKey: String,

    @Enumerated(EnumType.STRING)
    var platform: OAuthProviderEnum,

    @JsonBackReference
    var providerId: String,

    @JsonBackReference
    var jwtToken: String,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var deviceTokens : MutableSet<FCMToken>

) : BaseEntity(){
    override fun equals(other: Any?): Boolean {
        return providerId == (other as User).providerId

    }
    override fun hashCode(): Int {
        return Objects.hash(userId, userName, userNickname, role, profileImageKey, platform, providerId, jwtToken, isDeleted)
    }


}