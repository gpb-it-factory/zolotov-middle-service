package ru.gpb.midserv.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.gpb.midserv.models.Response
import ru.gpb.midserv.models.User
import ru.gpb.midserv.service.RegisterService
import ru.gpb.midserv.service.ServerApiResponse

@RestController
class RegisterController(private val registerService: RegisterService) {

    private val logger = LoggerFactory.getLogger(RegisterController::class.java)

    @PostMapping("/register")
    fun receiveUser(@RequestBody user: User): ResponseEntity<Response> {
        logger.info("Receive user from frontend")
        val operationCode = registerService.registerUser(user)
        return when (operationCode) {
            is ServerApiResponse.Success -> {
                logger.info("Return SUCCESS to frontend")
                ResponseEntity.noContent().build()
            }

            is ServerApiResponse.Problem -> {
                logger.info("Return PROBLEM to frontend")
                ResponseEntity(
                    Response("User already registered"), HttpStatus.CONFLICT
                )
            }

            is ServerApiResponse.Error -> {
                logger.info("Return ERROR to frontend")
                ResponseEntity(
                    Response("Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR
                )
            }
        }
    }
}
