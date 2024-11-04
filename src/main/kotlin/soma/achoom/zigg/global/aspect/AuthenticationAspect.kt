package soma.achoom.zigg.global.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Aspect
@Component
class AuthenticationAspect {
    @Pointcut("@annotation(soma.achoom.zigg.auth.annotation.AuthenticationValidation)")
    fun authenticationValidate(joinPoint: JoinPoint) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated) {
            throw AccessDeniedException("Authentication required")
        }
    }
}