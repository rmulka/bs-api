package com.rmulka.bs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.rmulka.bs.domain.GameDetails
import com.rmulka.bs.domain.PlayerTurn
import com.rmulka.bs.exception.ResourceNotFoundException
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
        val playerOrder = (1..players.size).zip((1..players.size).shuffled()).toMap()

        val playerCards = players.map { it.id }.associateWith { mutableListOf<Card>() }

        playerCards.keys.toList().loopWithRepeatWhile({ !deck.isEmpty() }) {
            playerCards[it]?.add(deck.dealCard())
        }

        // Player who holds the Ace of Spades goes first
        val startingTurn = playerCards.entries.find { it.value.any { card -> card == Card(1, 1) } }?.let {
            playerOrder.entries.find { (_, value) -> value == playerIdNumMap[it.key] }?.key
        }

        val gameDetails = GameDetails(
                playerIdNumberMap = playerIdNumMap,
                playerOrder = playerOrder,
                currentTurn = startingTurn,
                playerCards = playerCards,
                pile = listOf(),
                isWinner = false,
                winnerName = null,
                numCardsLastPlayed = null,
                lastPlayedRank = null,
                currentRank = 1,
                firstTurn = true
        )

        return gameDao.startGame(gameId, JSONB.jsonb(objectMapper.writeValueAsString(gameDetails))).let { dbGame ->
            GameResponse(dbGame, converterUtil.toGameDetails(dbGame.details), players)
        }
    }

    @Transactional
    fun processTurn(gameId: UUID, playerTurn: PlayerTurn): GameResponse {
        val game = gameDao.fetchGame(gameId)
        val gameDomain = converterUtil.toGameDomain(game)
        val players = playerGameDao.fetchPlayersByGameId(gameId).toPlayerDomain()

        val playedCards = playerTurn.playedCards.toSet()
        val oldCards = gameDomain.details.playerCards?.get(playerTurn.playerId)?.toSet()
                ?: throw ResourceNotFoundException("Player ${playerTurn.playerId} cards not found")

        val newPlayerCards = gameDomain.details.playerCards?.entries?.fold(mapOf<UUID, List<Card>>()) { acc, entry ->
            when {
                entry.key != playerTurn.playerId -> acc.plus(entry.toPair())
                else -> acc.plus(Pair(playerTurn.playerId, (oldCards - playedCards).toList()))
            }
        }

        val pile = gameDomain.details.pile?.plus(playerTurn.playedCards)
        val lastPlayedRank = gameDomain.details.currentRank ?: throw ResourceNotFoundException("Game $gameId current rank not found")

        val newGameDetails = GameDetails(
                playerIdNumberMap = gameDomain.details.playerIdNumberMap,
                playerOrder = gameDomain.details.playerOrder,
                currentTurn = gameDomain.details.currentTurn?.let { currentTurn -> currentTurn % game.numPlayers + 1 },
                playerCards = newPlayerCards,
                pile = pile,
                isWinner = false,
                winnerName = null,
                numCardsLastPlayed = playedCards.size,
                lastPlayedRank = lastPlayedRank,
                currentRank = (lastPlayedRank % 13) + 1,
                firstTurn = false
        )

        return gameDao.updateGameDetails(game, JSONB.jsonb(objectMapper.writeValueAsString(newGameDetails))).let { dbGame ->
            GameResponse(dbGame, converterUtil.toGameDetails(dbGame.details), players).also {
                logger.info("Game $gameId updated...Player ${playerTurn.playerId} played ${playerTurn.playedCards.size} ${playerTurn.playedCards.first().rank}'s")
            }
        }
    }
}