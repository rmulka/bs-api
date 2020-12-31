package com.rmulka.bs.response

import java.util.UUID

data class BasicGameResponse(
        val id: UUID,
        val inProgress: Boolean,
        val numPlayers: Int,
        val creatorName: String
)