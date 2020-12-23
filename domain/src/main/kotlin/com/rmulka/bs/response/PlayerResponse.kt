package com.rmulka.bs.response

import com.rmulka.bs.jooq.generated.tables.pojos.Player
import java.util.UUID

data class PlayerResponse(

        val id: UUID,
        val name: String
) {
    constructor(player: Player): this(player.id, player.playerName)
}