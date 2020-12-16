package com.rmulka.bs.domain

import java.util.UUID

data class PlayerGameDomain(

        val playerId: UUID,
        val gameId: UUID,
)