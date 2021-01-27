package com.rmulka.bs.domain

import com.rmulka.bs.jooq.generated.tables.pojos.Player

data class PlayerGameDetails(

        val game: GameDetails,
        val players: List<Player>
)