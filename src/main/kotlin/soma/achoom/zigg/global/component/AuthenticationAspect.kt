package soma.achoom.zigg.global.component

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Aspect
@Component
class AuthenticationAspect {
    @Pointcut("@annotation(soma.achoom.zigg.global.annotation.AuthenticationValidate)")
    fun authenticationValidate(joinPoint: JoinPoint) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated) {
            throw AccessDeniedException("Authentication required")
        }
    }
}