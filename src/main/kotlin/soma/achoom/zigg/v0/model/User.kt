package soma.achoom.zigg.v0.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.v0.auth.OAuthProviderEnum
import soma.achoom.zigg.v0.model.enums.UserRole


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

    var profileImageUrl:String = "default",

    @Enumerated(EnumType.STRING)
    @JsonBackReference
    var provider: OAuthProviderEnum,

    @JsonBackReference
    var providerId:String
) : BaseEntity()