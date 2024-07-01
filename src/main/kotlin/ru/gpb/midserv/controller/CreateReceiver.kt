package ru.gpb.midserv.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.gpb.midserv.models.Response
import ru.gpb.midserv.models.User
import ru.gpb.midserv.service.create.CreateAccountApiResponse
import ru.gpb.midserv.service.create.CreateService

@RestController
class CreateController(private val createService: CreateService) {

    private val logger = LoggerFactory.getLogger(RegisterController::class.java)

    @PostMapping("/createaccount")
    fun receiveUser(@RequestBody user: User): ResponseEntity<Response> {
        logger.info("Receive user from frontend")
        val operationCode = createService.createAccount(user)
        return when (operationCode) {
            is CreateAccountApiResponse.Success -> {
                logger.info("Return SUCCESS to frontend")
                ResponseEntity.noContent().build()
            }

            is CreateAccountApiResponse.Forbidden -> {
                logger.info("Return FORBIDDEN to frontend")
                ResponseEntity(
                    Response("User got to register first"), HttpStatus.FORBIDDEN
                )
            }

            is CreateAccountApiResponse.Problem -> {
                logger.info("Return PROBLEM to frontend")
                ResponseEntity(
                    Response("User already registered"), HttpStatus.CONFLICT
                )
            }

            is CreateAccountApiResponse.Error -> {
                logger.info("Return ERROR to frontend")
                ResponseEntity(
                    Response("Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR
                )
            }
        }
    }
}
