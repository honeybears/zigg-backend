package soma.achoom.zigg.v0.ai.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import soma.achoom.zigg.v0.ai.dto.GenerateThumbnailRequestDto
import soma.achoom.zigg.v0.ai.dto.GenerateThumbnailResponseDto

@Service
class AIService(webClientBuilder: WebClient.Builder, @Value("\${fastapi.default.url}") private val fastAPIUrl: String) {

    private val webClient = webClientBuilder.baseUrl(fastAPIUrl).build()

    suspend fun fetchDataFromFastAPI(): String = coroutineScope {
        println(fastAPIUrl)
        webClient.get()
            .uri("/")
            .retrieve()
            .awaitBody()
    }

    suspend fun createThumbnailRequest(generateThumbnailRequestDto: GenerateThumbnailRequestDto): GenerateThumbnailResponseDto = withContext(Dispatchers.IO) {
        val response = webClient.post()
            .uri("/fastapi/v0/thumbnail")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(generateThumbnailRequestDto)
            .retrieve()
            .awaitBody<GenerateThumbnailResponseDto>()
        return@withContext response
    }
}
