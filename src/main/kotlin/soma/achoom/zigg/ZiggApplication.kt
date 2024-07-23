package soma.achoom.zigg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaAuditing
class ZiggApplication{
	companion object {
		init {
			System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<ZiggApplication>(*args)
}
