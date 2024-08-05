package soma.achoom.zigg.v0.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.service.AIService

@RestController
@RequestMapping("/test")
class TestController @Autowired constructor(
    private val aiService: AIService
){
    @GetMapping
    suspend fun testFastAPIConnection() = aiService.fetchDataFromFastAPI()
}