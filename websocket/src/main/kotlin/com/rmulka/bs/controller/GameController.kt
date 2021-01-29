package com.rmulka.bs.controller

import com.rmulka.bs.request.BsRequest
import com.rmulka.bs.request.MessageRequest
import com.rmulka.bs.request.PlayerTurnRequest
import com.rmulka.bs.response.GameResponse
import com.rmulka.bs.response.MessageResponse
import com.rmulka.bs.service.GameService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import java.util.UUID

@Controller
class GameController(private val gameService: GameService) {

    @MessageMapping("/game/details/{gameId}")
    @SendTo("/topic/game/{gameId}")
    fun fetchGame(@DestinationVariable gameId: UUID): GameResponse = gameService.fetchGame(gameId)

    @MessageMapping("/game/start/{gameId}")
    @SendTo("/topic/game/{gameId}")
    fun startGame(@DestinationVariable gameId: UUID): GameResponse = gameService.startGame(gameId)

    @MessageMapping("/game/turn/{gameId}")
    @SendTo("/topic/game/{gameId}")
    fun processTurn(
            @DestinationVariable gameId: UUID,
            playerTurnRequest: PlayerTurnRequest
    ): GameResponse = gameService.processTurn(gameId, playerTurnRequest.toPlayerTurn())

    @MessageMapping("/game/update/{gameId}/bs")
    @SendTo("/topic/game/{gameId}")
    fun processBS(
            @DestinationVariable gameId: UUID,
            request: BsRequest
    ): GameResponse = gameService.processBs(gameId, request.playerId)

    @MessageMapping("/game/miss/{gameId}/player/{playerId}")
    @SendTo("/topic/game/{gameId}")
    fun processMissedTurn(
            @DestinationVariable gameId: UUID,
            @DestinationVariable playerId: UUID
    ): GameResponse = gameService.processMissedTurn(gameId, playerId)

    @MessageMapping("/chat/{gameId}")
    @SendTo("/topic/chat/{gameId}")
    fun processChatMessage(
            @DestinationVariable gameId: UUID,
            messageRequest: MessageRequest
    ): MessageResponse = gameService.processChatMessage(gameId, messageRequest.playerId, messageRequest.message)
}