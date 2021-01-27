package com.rmulka.bs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BsApplication

fun main(args: Array<String>) {
    runApplication<BsApplication>(*args)
}