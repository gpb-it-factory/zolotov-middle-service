package ru.gpb.midserv

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.gpb.midserv.controller.RegisterController
import ru.gpb.midserv.models.User
import ru.gpb.midserv.service.register.RegisterService
import ru.gpb.midserv.service.register.ServerApiResponse

@SpringBootTest
class MiddleServiceApplicationTests {

    @Test
    fun testRegister() {

        var testCount = 1
        val testUser = User(21, "Pabl0")
        val client = mockk<WebClient>()
        val service = RegisterService(client)
        val controller = RegisterController(service)

        every {
            client
                .post()
                .uri("/v2/users")
                .contentType(any())
                .body(any(), User::class.java)
                .exchangeToMono<ServerApiResponse>(any())
                .timeout(any())
        } answers {
            when (testCount++) {
                1 -> Mono.just(ServerApiResponse.Success())
                2 -> Mono.just(ServerApiResponse.Problem())
                else -> Mono.just(ServerApiResponse.Error())
            }
        }


        Assertions.assertEquals(HttpStatus.NO_CONTENT, controller.receiveUser(testUser).statusCode)
        Assertions.assertEquals(HttpStatus.CONFLICT, controller.receiveUser(testUser).statusCode)
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.receiveUser(testUser).statusCode)
    }

}
