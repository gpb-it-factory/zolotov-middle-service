package ru.gpb.midserv.service.register

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.gpb.midserv.models.User
import java.time.Duration

@Service
class RegisterService(private val webClient: WebClient) {

    private val logger = LoggerFactory.getLogger(RegisterService::class.java)

    fun registerUser(user: User): ServerApiResponse {
        logger.info("User sent to backend")
        val response = webClient.post()
            .uri("/v2/users")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(user), User::class.java)
            .exchangeToMono { transformResponseByCodes(it) }
            .timeout(Duration.ofSeconds(5))

        return try {
            response.block() ?: ServerApiResponse.Error()
        } catch (e: Exception) {
            ServerApiResponse.Error()
        }
    }

    private fun transformResponseByCodes(item: ClientResponse): Mono<ServerApiResponse> {
        return when {
            item.statusCode().isSameCodeAs(HttpStatus.NO_CONTENT) -> {
                Mono.just(ServerApiResponse.Success())
            }

            item.statusCode().isSameCodeAs(HttpStatus.CONFLICT) -> {
                item.bodyToMono(ServerApiResponse.Problem::class.java)
            }

            else -> {
                item.bodyToMono(ServerApiResponse.Error::class.java)
            }
        }
    }
}
