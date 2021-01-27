package com.rmulka.bs.request

import com.rmulka.bs.domain.PlayerGameDomain
import java.util.UUID

data class PlayerGameRequest(
        val player_id: UUID,
        val game_id: UUID
) {
    fun toPlayerGameDomain(): PlayerGameDomain = PlayerGameDomain(playerId=player_id, gameId=game_id)
}