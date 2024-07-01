package ru.gpb.midserv.service.create

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
class CreateService(private val webClient: WebClient) {

    private val logger = LoggerFactory.getLogger(CreateService::class.java)

    fun createAccount(user: User): CreateAccountApiResponse {
        logger.info("User sent to backend")

        val registrationCheck = checkRegistration(user.userId).block()

        val createAccountResponse = webClient.post()
            .uri("/v2/users/${user.userId}/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(user), User::class.java)
            .exchangeToMono { transformCreateResponseByCodes(it) }
            .timeout(Duration.ofSeconds(5))

        return try {
            when (registrationCheck) {
                is CreateAccountApiResponse.Success -> {
                    createAccountResponse.block() ?: CreateAccountApiResponse.Error()
                }

                else -> {
                    CreateAccountApiResponse.Forbidden()
                }
            }
        } catch (e: Exception) {
            CreateAccountApiResponse.Error()
        }
    }

    private fun checkRegistration(id: Long) = webClient.get()
        .uri("/v2/users/$id")
        .exchangeToMono { transformRegistrationResponseByCodes(it) }
        .timeout(Duration.ofSeconds(5))


    private fun transformCreateResponseByCodes(item: ClientResponse): Mono<CreateAccountApiResponse> {
        return when {
            item.statusCode().isSameCodeAs(HttpStatus.OK) || item.statusCode().isSameCodeAs(HttpStatus.NO_CONTENT) -> {
                Mono.just(CreateAccountApiResponse.Success())
            }

            item.statusCode().isSameCodeAs(HttpStatus.CONFLICT) -> {
                Mono.just(CreateAccountApiResponse.Problem())
            }

            else -> {
                item.bodyToMono(CreateAccountApiResponse.Error::class.java)
            }
        }
    }

    private fun transformRegistrationResponseByCodes(item: ClientResponse): Mono<CreateAccountApiResponse> {
        return when {
            item.statusCode().isSameCodeAs(HttpStatus.OK) -> {
                Mono.just(CreateAccountApiResponse.Success())
            }

            else -> {
                item.bodyToMono(CreateAccountApiResponse.Error::class.java)
            }
        }
    }
}
