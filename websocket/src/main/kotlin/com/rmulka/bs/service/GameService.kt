package com.rmulka.bs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.rmulka.bs.domain.GameDetails
import com.rmulka.bs.game.Card
import com.rmulka.bs.game.Deck
import com.rmulka.bs.repository.GameDao
import com.rmulka.bs.repository.PlayerGameDao
import com.rmulka.bs.response.GameResponse
import com.rmulka.bs.util.ConverterUtil
import com.rmulka.bs.utils.buildGameResponse
import com.rmulka.bs.utils.loopWithRepeatWhile
import com.rmulka.bs.utils.toPlayerDomain
import mu.KotlinLogging
import org.jooq.JSONB
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GameService(private val gameDao: GameDao,
                  private val playerGameDao: PlayerGameDao,
                  private val objectMapper: ObjectMapper,
                  private val converterUtil: ConverterUtil) {

    companion object {
        val logger = KotlinLogging.logger {}
    }

    @Transactional(readOnly = true)
    fun fetchGame(gameId: UUID): GameResponse = buildGameResponse(
            converterUtil.toGameDomain(gameDao.fetchGame(gameId)),
            playerGameDao.fetchPlayersByGameId(gameId)
    )

    /**
     * Method for game creator to start a game once 3-8 players have joined
     *
     * @param gameId: UUID of the game
     *
     * This method will:
     *    Create deck and shuffle.
     *    Set player order
     *    Deal cards to each player
     *    Update the database with new game details
     *    Return game response
     */
    @Transactional
    fun startGame(gameId: UUID): GameResponse {
        logger.info("Starting game $gameId")

        val deck = Deck().shuffle()
        val players = playerGameDao.fetchPlayersByGameId(gameId).toPlayerDomain()

        val playerIdNumMap = players.foldIndexed(mapOf<UUID, Int>()) { idx, acc, player -> acc.plus(Pair(player.id, idx + 1)) }
        val playerOrder = (1..players.size).shuffled()

        val playerCards = players.map { it.id }.associateWith { mutableListOf<Card>() }

        playerCards.keys.toList().loopWithRepeatWhile({ !deck.isEmpty() }) {
            playerCards[it]?.add(deck.dealCard())
        }

        val gameDetails = GameDetails(
                playerIdNumberMap = playerIdNumMap,
                playerOrder = playerOrder,
                currentTurn = 1,
                playerCards = playerCards,
                pile = listOf(),
                isWinner = false,
                winnerName = null,
                numCardsLastPlayed = null,
                lastPlayedCard = null
        )

        return gameDao.startGame(gameId, JSONB.jsonb(objectMapper.writeValueAsString(gameDetails))).let { dbGame ->
            GameResponse(dbGame, converterUtil.toGameDetails(dbGame.details), players)
        }
    }
}