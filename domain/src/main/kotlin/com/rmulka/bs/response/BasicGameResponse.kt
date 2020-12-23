package com.rmulka.bs.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BasicGameResponse(
        val id: UUID,
        val inProgress: Boolean,
        val numPlayers: Int,
        val creatorName: String
)