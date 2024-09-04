package soma.achoom.zigg

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestConfig::class)
class ZiggApplicationTests {

	@Test
	fun contextLoads() {
	}

}
