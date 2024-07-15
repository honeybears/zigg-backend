package soma.achoom.zigg.v0.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.SecurityFilterChain
import soma.achoom.zigg.v0.service.UserService
import kotlin.math.log

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuthService: CustomOAuthService
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
         http
            .csrf{
                it.disable()
            }
             .authorizeHttpRequests {
                 it.requestMatchers("/login","/login/oauth2/code/**","/oauth2/authorization/**","/health").permitAll()
                     .anyRequest().authenticated()
             }
            .oauth2Login {
//                 it.loginProcessingUrl("login/oauth2/code/google")
                     it.userInfoEndpoint {
                         it.userService(customOAuthService)
                     }
                     .defaultSuccessUrl("/api/v0/user/info")
             }


            .logout{
                it.logoutSuccessUrl("/api/logout")
            }

            return http.build()
    }
    
}