package com.rmulka.bs.controller

import com.rmulka.bs.domain.Game
import com.rmulka.bs.service.GameService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class GameController(private val gameService: GameService) {

    @MessageMapping("/game/details/{gameId}")
    @SendTo("/topic/game/{gameId}")
    fun fetchGame(@DestinationVariable gameId: UUID): Game = gameService.fetchGame(gameId)
}