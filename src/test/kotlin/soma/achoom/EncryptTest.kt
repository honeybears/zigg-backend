package soma.achoom

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import soma.achoom.zigg.ZiggApplication

@SpringBootTest(classes = [ZiggApplication::class]) // 애플리케이션 메인 클래스를 지정합니다.
@ActiveProfiles("test")
class EncryptTest {
    @Autowired
    @Qualifier(value = "jasyptStringEncryptor")
    lateinit var encryptor: PooledPBEStringEncryptor;

    @Test
    fun getEncrypt() {
        println(encryptor.encrypt("test"))
    }
}