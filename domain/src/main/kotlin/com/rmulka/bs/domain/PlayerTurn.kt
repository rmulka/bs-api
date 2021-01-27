package com.rmulka.bs.domain

import com.rmulka.bs.game.Card
import java.util.UUID

data class PlayerTurn(
        val playerId: UUID,
        val playedCards: List<Card>,
        val remainingCards: List<Card>
)