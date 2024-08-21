package soma.achoom.zigg

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
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
