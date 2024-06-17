package ru.gpb.midserv.service

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.gpb.midserv.models.User

@Service
class RegisterService(private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(RegisterService::class.java)

    fun registerUser(user: User): ServerApiResponse {
        logger.info("User sent to backend")
        val response = restTemplate.postForEntity("/register", user, Error::class.java)
        return transform(response)
    }

    private fun transform(item: ResponseEntity<Error>): ServerApiResponse {
        return when {
            item.statusCode.is2xxSuccessful -> ServerApiResponse.Success()

            item.statusCode.is4xxClientError -> {
                ServerApiResponse.Problem(
                    item.body!!.message,
                    item.body!!.type,
                    item.body!!.code,
                    item.body!!.traceId
                )
            }

            else -> ServerApiResponse.Error(
                item.body!!.message,
                item.body!!.type,
                item.body!!.code,
                item.body!!.traceId
            )
        }
    }
}
