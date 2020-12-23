package com.rmulka.bs.util

import com.rmulka.bs.jooq.generated.tables.pojos.Game
import com.rmulka.bs.response.BasicGameResponse

fun List<Game>.toGameResponse(): List<BasicGameResponse> =
        this.map { BasicGameResponse(it.id, it.inProgress, it.numPlayers, it.creatorName) }