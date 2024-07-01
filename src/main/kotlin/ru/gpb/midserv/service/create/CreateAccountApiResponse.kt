package ru.gpb.midserv.service.create


sealed class CreateAccountApiResponse {
    data class Success(
        val message: String = "Success"
    ) : CreateAccountApiResponse()

    data class Forbidden(
        val message: String = "",
        val type: String = "",
        val code: String = "",
        val traceId: String = ""
    ) : CreateAccountApiResponse()

    data class Problem(
        val message: String = "",
        val type: String = "",
        val code: String = "",
        val traceId: String = ""
    ) : CreateAccountApiResponse()

    data class Error(
        val message: String = "",
        val type: String = "",
        val code: String = "",
        val traceId: String = ""
    ) : CreateAccountApiResponse()
}
