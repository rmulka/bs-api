package com.rmulka.bs.domain

import com.rmulka.bs.game.Card
import java.util.UUID

data class GameDetails(
        val playerIdNumberMap: Map<UUID, Int> = emptyMap(),
        val playerOrder: Map<Int, Int> = emptyMap(),
        val currentTurn: Int? = null,
        val prevTurn: Int? = null,
        val playerCards: Map<UUID, List<Card>>? = null,
        val pile: List<Card> = listOf(),
        val isWinner: Boolean = false,
        val winnerName: String? = null,
        val numCardsLastPlayed: Int = 0,
        val lastPlayedRank: Int? = null,
        val currentRank: Int? = null,
        val firstTurn: Boolean = true,
        val bsCalled: Boolean = false,
        val isBs: Boolean = false,
        val bsPlayerId: UUID? = null,
        val playerCalledBsId: UUID? = null
)