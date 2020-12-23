package com.rmulka.bs.controller

import com.rmulka.bs.domain.Game
import com.rmulka.bs.response.BasicGameResponse
import com.rmulka.bs.request.PlayerGameRequest
import com.rmulka.bs.response.PlayerGameResponse
import com.rmulka.bs.service.GameService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = ["http://localhost:3000"])
class GameController(private val gameService: GameService) {

    @GetMapping
    suspend fun getAllGames(): ResponseEntity<List<BasicGameResponse>> =
            ResponseEntity.ok(gameService.fetchAllGames())

    @GetMapping("/{game_id}")
    suspend fun getGameDetails(@PathVariable("game_id") gameId: UUID): ResponseEntity<Game> =
            gameService.fetchGame(gameId).let { ResponseEntity.ok(it) }

    @PostMapping
    suspend fun createGame(@RequestHeader("user_id") @NotBlank userId: UUID): ResponseEntity<UUID> =
        gameService.createGame(userId).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @PostMapping("/players")
    suspend fun joinGame(@RequestBody playerGameRequest: PlayerGameRequest): ResponseEntity<PlayerGameResponse> =
            when (gameService.joinGame(playerGameRequest.toPlayerGameDomain())) {
                true -> ResponseEntity.ok(PlayerGameResponse())
                else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PlayerGameResponse("Game ${playerGameRequest.game_id} is full"))
            }

    @DeleteMapping("/players")
    suspend fun leaveGame(@RequestBody playerGameRequest: PlayerGameRequest): ResponseEntity<PlayerGameResponse> =
            gameService.leaveGame(playerGameRequest.toPlayerGameDomain()).let { ResponseEntity.ok(it) }
}