package ru.gpb.midserv

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MidservApplication

fun main(args: Array<String>) {
	runApplication<MidservApplication>(*args)
}
