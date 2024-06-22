package ru.gpb.midserv.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class Configuration {
    @Bean
    fun webClient(@Value("\${api.rest.url}") middleUrl: String): WebClient {
        return WebClient.builder()
            .baseUrl(middleUrl)
            .build()
    }
}