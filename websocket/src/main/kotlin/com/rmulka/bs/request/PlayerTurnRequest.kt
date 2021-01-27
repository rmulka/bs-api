package com.rmulka.bs.request

import com.rmulka.bs.domain.PlayerTurn
import com.rmulka.bs.game.Card
import java.util.UUID

data class PlayerTurnRequest(
        val playerId: UUID,
        val playedCards: List<Card>,
        val remainingCards: List<Card>
) {
    fun toPlayerTurn(): PlayerTurn = PlayerTurn(
            this.playerId,
            this.playedCards,
            this.remainingCards
    )
}