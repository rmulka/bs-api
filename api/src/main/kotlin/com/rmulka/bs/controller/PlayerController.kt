package com.rmulka.bs.controller

import com.rmulka.bs.request.PlayerRequest
import com.rmulka.bs.response.PlayerResponse
import com.rmulka.bs.service.PlayerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/players")
@CrossOrigin(origins = ["http://localhost:3000"])
class PlayerController(private val playerService: PlayerService) {

    @PostMapping
    suspend fun createPlayer(@RequestBody @Valid playerRequest: PlayerRequest): ResponseEntity<PlayerResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.createPlayer(playerRequest.toPlayerDomain()))
    }
}