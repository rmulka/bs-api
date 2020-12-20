package com.rmulka.bs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.rmulka.bs.domain.GameDetails
import com.rmulka.bs.domain.PlayerGameDomain
import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.jooq.generated.tables.pojos.Game
import com.rmulka.bs.repository.GameDao
import com.rmulka.bs.repository.PlayerDao
import com.rmulka.bs.repository.PlayerGameDao
import com.rmulka.bs.response.GameResponse
import com.rmulka.bs.response.DetailGameResponse
import com.rmulka.bs.response.PlayerGameResponse
import com.rmulka.bs.util.toGameResponse
import mu.KotlinLogging
import org.jooq.JSONB
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GameService(private val gameDao: GameDao,
                  private val playerDao: PlayerDao,
                  private val playerGameDao: PlayerGameDao,
                  private val objectMapper: ObjectMapper) {

    companion object {
        val logger = KotlinLogging.logger {}
    }

    @Transactional
    suspend fun joinGame(playerGameDomain: PlayerGameDomain): Boolean =
        gameDao.fetchGame(playerGameDomain.gameId).let { game ->
            when {
                game.numPlayers >= 8 -> false.also {
                    logger.info("Player ${playerGameDomain.playerId} attempted to join full game ${playerGameDomain.gameId}")
                }
                !game.isActive -> false.also {
                    logger.info("Player ${playerGameDomain.playerId} attempted to join inactive game ${playerGameDomain.gameId}")
                }
                else -> true.also {
                    playerGameDao.joinGame(playerGameDomain, game.numPlayers == 0)
                    gameDao.addPlayer(playerGameDomain.gameId)
                    logger.info("Player ${playerGameDomain.playerId} joined game ${playerGameDomain.gameId}" +
                            if (game.numPlayers == 0) " as game creator" else "")
                }
            }
        }

    suspend fun fetchGame(gameId: UUID): DetailGameResponse =
            toDetailGameResponse(gameDao.fetchGame(gameId))

    suspend fun createGame(userId: UUID): UUID =
            playerDao.fetchOneById(userId)?.let { player ->
                gameDao.createGame(player, generateGameJson()).also { gameId ->
                    logger.info("Game $gameId has been created by user $userId")
                }
            } ?: throw ResourceNotFoundException("Player id $userId not found")

    suspend fun fetchAllGames(): List<GameResponse> = gameDao.fetchAllGames().toGameResponse()

    @Transactional
    suspend fun leaveGame(playerGameDomain: PlayerGameDomain): PlayerGameResponse =
        playerGameDao.fetchByPlayerId(playerGameDomain.playerId).let { playerGame ->
            playerGameDao.leaveGame(playerGameDomain.playerId)
            gameDao.fetchGame(playerGameDomain.gameId).let { game ->
                when {
                    game.inProgress -> {
                        logger.info("Game ${game.id} was in progress when player ${playerGameDomain.playerId} left...soft deleting")
                        gameDao.deleteGame(playerGameDomain.gameId)
                        playerGameDao.deletePlayerGame(game.id)
                    }
                    game.numPlayers == 1 || game.creatorId == playerGameDomain.playerId -> {
                        logger.info("Player ${playerGameDomain.playerId} was game creator or last player in game ${game.id}...soft deleting")
                        gameDao.deleteGame(playerGameDomain.gameId)
                        playerGameDao.deletePlayerGame(game.id)
                    }
                    else -> {
                        gameDao.removePlayer(game.id)
                        playerGameDao.removePlayer(playerGameDomain.playerId)
                        logger.info("Player ${playerGameDomain.playerId} left game ${game.id}. Remaining players: ${game.numPlayers - 1}")
                    }
                }
            }
            PlayerGameResponse("${playerGame.gameId}")
        }

    private fun generateGameJson(): JSONB =
            JSONB.jsonb(objectMapper.writeValueAsString(GameDetails()))

    private fun toDetailGameResponse(game: Game): DetailGameResponse =
            DetailGameResponse(
                    id = game.id,
                    inProgress = game.inProgress,
                    details = objectMapper.readValue(game.details.toString(), GameDetails::class.java),
                    numPlayers = game.numPlayers
            )
}