package com.rmulka.bs.domain

import com.rmulka.bs.game.Card
import java.util.UUID

data class GameDetails(
        val playerIdNumberMap: Map<UUID, Int> = emptyMap(),
        val playerOrder: List<Int> = emptyList(),
        val currentTurn: Int? = null,
        val playerCards: Map<UUID, List<Card>>? = null,
        val pile: List<Card>? = null,
        val isWinner: Boolean = false,
        val winnerName: String? = null,
        val numCardsLastPlayed: Int? = null,
        val lastPlayedCard: Card? = null,
        val currentRank: Int? = null
)