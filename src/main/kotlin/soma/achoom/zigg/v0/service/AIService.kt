package soma.achoom.zigg.v0.service

import kotlinx.coroutines.coroutineScope
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class AIService(webClientBuilder: WebClient.Builder, @Value("\${fastapi.default.url}") private val fastAPIUrl: String) {

    private val webClient = webClientBuilder.baseUrl(fastAPIUrl).build()

    suspend fun fetchDataFromFastAPI(): String = coroutineScope {
        webClient.get()
            .uri("/")
            .retrieve()
            .awaitBody()
    }
}
