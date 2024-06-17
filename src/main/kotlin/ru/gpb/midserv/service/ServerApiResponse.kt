package ru.gpb.midserv.service

import kotlinx.serialization.Serializable

sealed class ServerApiResponse {
    data class Success(val message: String = "Success") : ServerApiResponse()

    data class Problem(val message: String, val type: String, val code: String, val traceId: String) :
        ServerApiResponse()

    data class Error(val message: String, val type: String, val code: String, val traceId: String) :
        ServerApiResponse()
}

@Serializable
data class Error(val message: String = "", val type: String = "", val code: String = "", val traceId: String = "")
