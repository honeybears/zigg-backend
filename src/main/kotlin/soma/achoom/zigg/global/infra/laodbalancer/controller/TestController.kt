package soma.achoom.zigg.global.infra.laodbalancer.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.global.infra.gcs.GCSService
import soma.achoom.zigg.ai.service.AIService

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
         return gcsService.getPreSignedGetUrl("test")
    }
}