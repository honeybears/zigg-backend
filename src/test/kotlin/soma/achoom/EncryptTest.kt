package soma.achoom

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import soma.achoom.zigg.ZiggApplication
import soma.achoom.zigg.firebase.config.FCMConfig

@SpringBootTest(classes = [ZiggApplication::class]) // 애플리케이션 메인 클래스를 지정합니다.
@ActiveProfiles("test")
class EncryptTest {
    @Mock
    lateinit var fcmConfig: FCMConfig

    @Autowired
    @Qualifier(value = "jasyptStringEncryptor")
    lateinit var encryptor: PooledPBEStringEncryptor;

    @Test
    fun getEncrypt() {
    }
}