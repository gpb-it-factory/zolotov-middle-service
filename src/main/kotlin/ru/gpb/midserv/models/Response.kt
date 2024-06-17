package ru.gpb.midserv.models

import kotlinx.serialization.Serializable

@Serializable
data class Response(val message: String = "")