package soma.achoom.zigg

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync
import java.util.*

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableAsync
@ConfigurationPropertiesScan // 모든 @ConfigurationProperties 클래스를 자동 스캔
class ZiggApplication{
	@PostConstruct
	fun init() {
		// Set the default timezone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	}
}
fun main(args: Array<String>) {
	runApplication<ZiggApplication>(*args)
}
