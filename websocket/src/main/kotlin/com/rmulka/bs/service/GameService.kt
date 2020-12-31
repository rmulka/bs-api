package com.rmulka.bs.service

import com.rmulka.bs.game.Deck
import com.rmulka.bs.repository.GameDao
import com.rmulka.bs.repository.PlayerGameDao
import com.rmulka.bs.response.GameResponse
import com.rmulka.bs.util.ConverterUtil
import com.rmulka.bs.util.buildGameResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameService(private val gameDao: GameDao,
                  private val playerGameDao: PlayerGameDao,
                  private val converterUtil: ConverterUtil) {

    companion object {
        val logger = KotlinLogging.logger {}
    }

    fun fetchGame(gameId: UUID): GameResponse = buildGameResponse(converterUtil.toGameDomain(gameDao.fetchGame(gameId)),
            playerGameDao.fetchPlayersByGameId(gameId))

    fun startGame(gameId: UUID): GameResponse {
        val deck = Deck().shuffle()
        val game = this.fetchGame(gameId)
        return game
    }
}