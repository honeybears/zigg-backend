package soma.achoom.zigg.v0.controller

import com.google.cloud.storage.HttpMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.service.AIService
import soma.achoom.zigg.v0.service.GCSService

@RestController
@RequestMapping("/test")
class TestController @Autowired constructor(
    private val aiService: AIService,
    private val gcsService: GCSService,
){
    @GetMapping
    suspend fun testFastAPIConnection() = aiService.fetchDataFromFastAPI()

    @GetMapping("/gcs")
    fun testGCSConnection() : String{
         return gcsService.preSignedUrl("test", 1000, HttpMethod.GET)
    }
}