package com.rmulka.bs.controller

import com.rmulka.bs.request.GameResponse
import com.rmulka.bs.request.PlayerGameRequest
import com.rmulka.bs.response.PlayerGameResponse
import com.rmulka.bs.service.GameService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/games")
class GameController(private val gameService: GameService) {

    @GetMapping
    suspend fun getAllGames(): ResponseEntity<List<GameResponse>> =
            ResponseEntity.ok(gameService.fetchAllGames())

    @PostMapping
    suspend fun createGame(): ResponseEntity<UUID> =
        gameService.createGame().let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @PostMapping("/players")
    suspend fun joinGame(@RequestBody @Valid playerGameRequest: PlayerGameRequest): ResponseEntity<PlayerGameResponse> =
            when (gameService.joinGame(playerGameRequest.toPlayerGameDomain())) {
                true -> ResponseEntity.ok(PlayerGameResponse())
                else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PlayerGameResponse("Game ${playerGameRequest.game_id} is full"))
            }
}