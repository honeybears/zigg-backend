package soma.achoom.zigg.global.component

import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.time.measureTime

@Aspect
@Slf4j
@Component
class LogAspect  {

    val log = LoggerFactory.getLogger(this.javaClass)

    @Pointcut("execution(* soma.achoom.zigg..*(..))")
    fun all() {

    }

    @Pointcut("execution(* soma.achoom.zigg.*.service.*.*(..))")
    fun service() {

    }

    @Pointcut("execution(* soma.achoom.zigg.*.controller.*.*(..))")
    fun controller() {

    }

    @Pointcut("execution(* soma.achoom.zigg.*.repository.*.*(..))")
    fun repository() {

    }

    @Around("service()||repository()")
    fun logging(joinPoint: ProceedingJoinPoint): Any? {
        var result: Any?
        val timeMs = measureTime {
            result = joinPoint.proceed()
        }.inWholeMilliseconds
        log.info("log = {}", joinPoint.signature)
        log.info("time = {}ms", timeMs)
        return result
    }

    @Before("controller()")
    private fun controllerRequestLogger(joinPoint: JoinPoint) {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        log.info("Method : ${method.name}")
    }
    @AfterThrowing("all()", throwing = "exception")
    fun exceptionThrowingLogger(joinPoint: JoinPoint, exception: Exception) {
        log.error("An exception has been thrown in ${joinPoint.signature.name}()", exception)
    }

    @After("controller()")
    fun controllerResponseLogger(joinPoint: JoinPoint) {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        log.info("Method : ${method.name}")
    }
}
