package ru.gpb.midserv.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.client.RestTemplate
import ru.gpb.midserv.utils.Constants
import ru.gpb.midserv.service.CustomResponseErrorHandler

@Configuration
class Configuration {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .rootUri(Constants.BACK)
            .defaultHeader("Content-Type", "application/json")
            .errorHandler(CustomResponseErrorHandler())
            .messageConverters(KotlinSerializationJsonHttpMessageConverter())
            .build()
    }
}