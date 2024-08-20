package soma.achoom.zigg.auth.filter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import soma.achoom.zigg.user.entity.User
import java.util.*


@Component
class JwtTokenProvider {
    @Value("\${jwt.secret}")
    lateinit var secretKey: String
    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    fun createTokenWithUserInfo(user: User, userInfo: Map<String, Any>): String {

        val userId = user.providerId
        val authorities = user.role

        val now = Date()

        val accessToken = Jwts.builder()
            .setSubject(userId)
            .addClaims(mapOf("auth" to authorities, "provider" to user.platform.name))
            .setIssuedAt(now)
            .signWith(key, io.jsonwebtoken.SignatureAlgorithm.HS256)
            .compact()
        return "Bearer $accessToken"
    }

    fun getAuthentication(token: String): Authentication {
        val claims: Claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰입니다.")
        val oauthProvider = claims["provider"] as String // Extract the OAuth provider
        val authorities: Collection<GrantedAuthority> = (auth as String)
            .split(",")
            .map { SimpleGrantedAuthority(it) }
        val userDetails = CustomUserDetails(claims.subject, "", oauthProvider, authorities)
        return UsernamePasswordAuthenticationToken(userDetails, null, authorities)
    }

    fun validateToken(token: String): Boolean {
        try {
            getClaims(token)
            return true
        } catch (e: Exception) {
            println(e.message)
        }
        return false
    }

    private fun getClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}