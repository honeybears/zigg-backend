package soma.achoom.zigg.v0.model

import jakarta.persistence.*
import soma.achoom.zigg.v0.auth.OAuthProviderEnum


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

    var image:String? = null,

    @Enumerated(EnumType.STRING)
    var provider: OAuthProviderEnum,

    var providerId:String
    ) : BaseEntity()