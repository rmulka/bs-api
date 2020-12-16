package com.rmulka.bs.domain

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.rmulka.bs.game.Card

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class GameDetails(
        val playerOrder: List<Int> = emptyList(),
        val currentTurn: Int? = null,
        val playerCards: Map<Int, List<Card>>? = null,
        val pile: List<Card>? = null,
        val isWinner: Boolean = false,
        val winnerName: String? = null,
        val lastRankPlayed: Int? = null,
        val lastPlayedCard: Card? = null
)