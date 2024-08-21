package soma.achoom.zigg.ai.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import soma.achoom.zigg.ai.dto.GenerateAiFeedbackRequestDto
import soma.achoom.zigg.ai.dto.GenerateThumbnailRequestDto
import soma.achoom.zigg.ai.dto.GenerateThumbnailResponseDto
import soma.achoom.zigg.ai.dto.YoutubeUrlRequestDto

@Service
class AIService(webClientBuilder: WebClient.Builder, @Value("\${fastapi.default.url}") private val fastAPIUrl: String) {
    private val webClient = webClientBuilder
        .baseUrl(fastAPIUrl)
        .build()

    suspend fun fetchDataFromFastAPI(): String = coroutineScope {
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

    suspend fun generateAIFeedbackRequest(generateAiFeedbackRequestDto: GenerateAiFeedbackRequestDto)
    = withContext(Dispatchers.IO) {
        val response = webClient.post()
            .uri("/fastapi/v0/gemini-feedback")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(generateAiFeedbackRequestDto)
            .retrieve()
            .onStatus({ it.is4xxClientError || it.is5xxServerError }) {
                throw RuntimeException("Error occurred while generating AI feedback")
            }
            .awaitBody<Any>()
        return@withContext
    }

    suspend fun putYoutubeVideoToGCS(youtubeUrlRequestDto: YoutubeUrlRequestDto) = withContext(Dispatchers.IO) {
        val response = webClient.post()
            .uri("/fastapi/v0/youtube")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(youtubeUrlRequestDto)
            .retrieve()
            .awaitBody<Any>()
        return@withContext

    }

}