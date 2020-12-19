package com.rmulka.bs.util

import com.rmulka.bs.jooq.generated.tables.pojos.Game
import com.rmulka.bs.response.GameResponse

fun List<Game>.toGameResponse(): List<GameResponse> =
        this.map { GameResponse(it.id, it.inProgress, it.numPlayers) }