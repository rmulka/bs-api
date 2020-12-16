package com.rmulka.bs.request

import java.util.UUID

data class GameResponse(

        val id: UUID,
        val inProgress: Boolean,
        val numPlayers: Int
)