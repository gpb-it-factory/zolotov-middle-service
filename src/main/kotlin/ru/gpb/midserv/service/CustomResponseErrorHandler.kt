package ru.gpb.midserv.service

import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler

@Component
class CustomResponseErrorHandler : ResponseErrorHandler {

    override fun hasError(clientHttpResponse: ClientHttpResponse): Boolean {
        return clientHttpResponse.statusCode.is5xxServerError || clientHttpResponse.statusCode.is4xxClientError
    }

    override fun handleError(clientHttpResponse: ClientHttpResponse) {}
}
