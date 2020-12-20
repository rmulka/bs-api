package com.rmulka.bs.controller

import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.response.ErrorResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange

@ControllerAdvice
class ControllerAdvice {

    companion object {
        val logger = KotlinLogging.logger {}
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    suspend fun handleResourceNotFoundException(ex: ResourceNotFoundException, request: ServerWebExchange): ResponseEntity<ErrorResponse> {
        logger.error("Request to ${request.request.uri} threw ResourceNotFoundException: ${ex.message}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}