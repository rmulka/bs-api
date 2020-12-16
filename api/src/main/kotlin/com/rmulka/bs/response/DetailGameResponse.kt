package com.rmulka.bs.response

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.rmulka.bs.domain.GameDetails
import java.util.UUID

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class DetailGameResponse(

        val id: UUID,
        val inProgress: Boolean,
        val numPlayers: Int,
        val details: GameDetails
)