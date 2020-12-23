package com.rmulka.bs.domain

import java.time.LocalDateTime
import java.util.UUID

data class Game(

        val id: UUID,
        val inProgress: Boolean,
        val details: GameDetails,
        val numPlayers: Int,
        val creatorName: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val isActive: Boolean,
        val creatorId: UUID
)