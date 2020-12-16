package com.rmulka.bs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.rmulka.bs.domain.GameDetails
import com.rmulka.bs.domain.PlayerGameDomain
import com.rmulka.bs.repository.GameDao
import com.rmulka.bs.repository.PlayerGameDao
import com.rmulka.bs.request.GameResponse
import com.rmulka.bs.util.toGameResponse
import org.jooq.JSONB
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GameService(private val gameDao: GameDao,
                  private val playerGameDao: PlayerGameDao,
                  private val objectMapper: ObjectMapper) {

    @Transactional
    suspend fun joinGame(playerGameDomain: PlayerGameDomain): Boolean =
        gameDao.getNumPlayersInGame(playerGameDomain.gameId).let { numPlayers ->
            if (numPlayers >= 8) false
            else {
                playerGameDao.joinGame(playerGameDomain, numPlayers == 0)
                gameDao.addPlayer(playerGameDomain.gameId)
                true
            }
        }

    suspend fun createGame(): UUID = gameDao.createGame(generateGameJson())

    suspend fun fetchAllGames(): List<GameResponse> = gameDao.fetchAllGames().toGameResponse()

    private fun generateGameJson(): JSONB =
            JSONB.jsonb(objectMapper.writeValueAsString(GameDetails()))
}