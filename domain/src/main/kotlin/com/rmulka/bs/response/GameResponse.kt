package com.rmulka.bs.response

import com.rmulka.bs.domain.GameDetails
import com.rmulka.bs.domain.Player
import java.time.LocalDateTime
import java.util.UUID

data class GameResponse(
        val id: UUID,
        val inProgress: Boolean,
        val details: GameDetails,
        val numPlayers: Int,
        val creatorName: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val isActive: Boolean,
        val creatorId: UUID,
        val players: List<Player>
)