package com.rmulka.bs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.rmulka.bs.domain.GameDetails
import com.rmulka.bs.domain.Player
import com.rmulka.bs.domain.PlayerTurn
import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.game.Card
import com.rmulka.bs.game.Deck
import com.rmulka.bs.repository.ChatDao
import com.rmulka.bs.repository.GameDao
import com.rmulka.bs.repository.PlayerGameDao
import com.rmulka.bs.response.GameResponse
import com.rmulka.bs.response.MessageResponse
import com.rmulka.bs.util.ConverterUtil
import com.rmulka.bs.utils.buildGameResponse
import com.rmulka.bs.utils.loopWithRepeatWhile
import com.rmulka.bs.utils.toPlayerDomain
import mu.KotlinLogging
import org.jooq.JSONB
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class GameService(private val gameDao: GameDao,
                  private val playerGameDao: PlayerGameDao,
                  private val chatDao: ChatDao,
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
        val playerNumberIdMap = playerIdNumMap.entries.map { (id, num) -> Pair(num, id) }.toMap()
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
                playerNumberIdMap = playerNumberIdMap,
                playerOrder = playerOrder,
                currentTurn = startingTurn,
                prevTurn = null,
                playerCards = playerCards,
                pile = listOf(),
                isWinner = false,
                winnerId = null,
                numCardsLastPlayed = 0,
                lastPlayedRank = null,
                currentRank = 1,
                firstTurn = true,
                bsCalled = false,
                isBs = false,
                bsPlayerId = null,
                playerCalledBsId = null,
                timerStart = OffsetDateTime.now()
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

        val prevPlayerUuid = gameDomain.details.playerNumberIdMap[gameDomain.details.playerOrder[gameDomain.details.prevTurn]]
        val (isWinner, winnerId) = checkForWinner(gameDomain.details.playerCards, prevPlayerUuid, players, gameId, false)

        val playedCards = playerTurn.playedCards.toSet()
        val oldCards = gameDomain.details.playerCards[playerTurn.playerId]?.toSet()
                ?: throw ResourceNotFoundException("Player ${playerTurn.playerId} cards not found")

        val newPlayerCards = gameDomain.details.playerCards.entries.fold(mapOf<UUID, List<Card>>()) { acc, entry ->
            when {
                entry.key != playerTurn.playerId -> acc.plus(entry.toPair())
                else -> acc.plus(Pair(playerTurn.playerId, (oldCards - playedCards).toList()))
            }
        }

        val pile = gameDomain.details.pile.plus(playerTurn.playedCards)
        val lastPlayedRank = gameDomain.details.currentRank ?: throw ResourceNotFoundException("Game $gameId current rank not found")

        val newGameDetails = GameDetails(
                playerIdNumberMap = gameDomain.details.playerIdNumberMap,
                playerNumberIdMap = gameDomain.details.playerNumberIdMap,
                playerOrder = gameDomain.details.playerOrder,
                currentTurn = gameDomain.details.currentTurn?.let { currentTurn -> currentTurn % game.numPlayers + 1 },
                prevTurn = gameDomain.details.currentTurn,
                playerCards = newPlayerCards,
                pile = pile,
                isWinner = isWinner,
                winnerId = winnerId,
                numCardsLastPlayed = playedCards.size,
                lastPlayedRank = lastPlayedRank,
                currentRank = (lastPlayedRank % 13) + 1,
                firstTurn = false,
                bsCalled = false,
                isBs = false,
                bsPlayerId = null,
                playerCalledBsId = null,
                timerStart = OffsetDateTime.now()
        )

        return gameDao.updateGameDetails(game, JSONB.jsonb(objectMapper.writeValueAsString(newGameDetails))).let { dbGame ->
            GameResponse(dbGame, converterUtil.toGameDetails(dbGame.details), players).also {
                logger.info("Game $gameId updated...Player ${playerTurn.playerId} played ${playerTurn.playedCards.size} $lastPlayedRank(s)")
            }
        }
    }

    @Transactional
    fun processMissedTurn(gameId: UUID, playerId: UUID): GameResponse {
        val game = gameDao.fetchGame(gameId)
        val gameDomain = converterUtil.toGameDomain(game)
        val players = playerGameDao.fetchPlayersByGameId(gameId).toPlayerDomain()

        val newPlayerCards = gameDomain.details.playerCards.entries.fold(mapOf<UUID, List<Card>>()) { acc, entry ->
            when {
                entry.key != playerId -> acc.plus(entry.toPair())
                else -> acc.plus(Pair(entry.key, (entry.value + gameDomain.details.pile).toList()))
            }
        }

        val lastPlayedRank = gameDomain.details.currentRank ?: throw ResourceNotFoundException("Game $gameId current rank not found")

        val newGameDetails = GameDetails(
                playerIdNumberMap = gameDomain.details.playerIdNumberMap,
                playerNumberIdMap = gameDomain.details.playerNumberIdMap,
                playerOrder = gameDomain.details.playerOrder,
                currentTurn = gameDomain.details.currentTurn?.let { currentTurn -> currentTurn % game.numPlayers + 1 },
                prevTurn = gameDomain.details.currentTurn,
                playerCards = newPlayerCards,
                pile = listOf(),
                isWinner = false,
                winnerId = null,
                numCardsLastPlayed = 0,
                lastPlayedRank = lastPlayedRank,
                currentRank = (lastPlayedRank % 13) + 1,
                firstTurn = false,
                bsCalled = false,
                isBs = false,
                bsPlayerId = null,
                playerCalledBsId = null,
                timerStart = OffsetDateTime.now()
        )

        return gameDao.updateGameDetails(game, JSONB.jsonb(objectMapper.writeValueAsString(newGameDetails))).let { dbGame ->
            GameResponse(dbGame, converterUtil.toGameDetails(dbGame.details), players).also {
                logger.info("Game $gameId updated...Player $playerId missed their turn. Picked up ${gameDomain.details.pile.size} cards")
            }
        }
    }

    @Transactional
    fun processBs(gameId: UUID, playerId: UUID): GameResponse {
        val game = gameDao.fetchGame(gameId)
        val gameDomain = converterUtil.toGameDomain(game)
        val players = playerGameDao.fetchPlayersByGameId(gameId).toPlayerDomain()

        if (gameDomain.details.bsCalled) return GameResponse(game, converterUtil.toGameDetails(game.details), players).also {
            logger.info("Game $gameId: player $playerId called BS but was already called...skipping")
        }

        val playerGettingBsId = gameDomain.details.playerNumberIdMap[gameDomain.details.playerOrder[gameDomain.details.prevTurn]]

        val lastCardsPlayed = gameDomain.details.pile.takeLast(gameDomain.details.numCardsLastPlayed)

        val isBs = lastCardsPlayed.any { card -> card.rank != gameDomain.details.lastPlayedRank }

        val (isWinner, winnerId) = checkForWinner(gameDomain.details.playerCards, playerGettingBsId, players, gameId, isBs)

        val newPlayerCards = gameDomain.details.playerCards.entries.fold(mapOf<UUID, List<Card>>()) { acc, entry ->
            when {
                isBs && entry.key == playerGettingBsId -> acc.plus(Pair(entry.key, entry.value + gameDomain.details.pile))
                !isBs && entry.key == playerId -> acc.plus(Pair(entry.key, entry.value + gameDomain.details.pile))
                else -> acc.plus(entry.toPair())
            }
        }

        val newGameDetails = GameDetails(
                playerIdNumberMap = gameDomain.details.playerIdNumberMap,
                playerNumberIdMap = gameDomain.details.playerNumberIdMap,
                playerOrder = gameDomain.details.playerOrder,
                currentTurn = gameDomain.details.currentTurn,
                prevTurn = gameDomain.details.prevTurn,
                playerCards = newPlayerCards,
                pile = listOf(),
                isWinner = isWinner,
                winnerId = winnerId,
                numCardsLastPlayed = gameDomain.details.numCardsLastPlayed,
                lastPlayedRank = gameDomain.details.lastPlayedRank,
                currentRank = gameDomain.details.currentRank,
                firstTurn = false,
                bsCalled = true,
                isBs = isBs,
                bsPlayerId = playerGettingBsId,
                playerCalledBsId = playerId,
                timerStart = gameDomain.details.timerStart
        )

        return gameDao.updateGameDetails(game, JSONB.jsonb(objectMapper.writeValueAsString(newGameDetails))).let { dbGame ->
            GameResponse(dbGame, converterUtil.toGameDetails(dbGame.details), players).also {
                logger.info("Game $gameId: player $playerId called BS and it was $isBs")
            }
        }
    }

    @Transactional
    fun processChatMessage(gameId: UUID, playerId: UUID, message: String): MessageResponse {
        val insertedMessage = chatDao.insertMessage(gameId, playerId, message)
        val gameMessages = chatDao.fetchMessages(gameId).map { chat -> Pair(chat.playerId, chat.message) }
        return MessageResponse(gameMessages).also {
            gameDao.gameUpdated(gameId, insertedMessage.createdAt)
        }
    }

    private fun checkForWinner(
            playerCards: Map<UUID, List<Card>>,
            playerId: UUID?,
            players: List<Player>,
            gameId: UUID,
            isBs: Boolean
    ): Pair<Boolean, UUID?> = (!isBs && playerCards[playerId]?.size == 0).let { isWinner ->
        val winnerId = if (isWinner) players.find { it.id == playerId }?.id.also {
            logger.info("Game $gameId: player $playerId is the winner")
        } else null

        Pair(isWinner, winnerId)
    }
}