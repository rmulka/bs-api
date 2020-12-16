package com.rmulka.bs.request

import com.rmulka.bs.domain.PlayerGameDomain
import java.util.UUID
import javax.validation.constraints.NotBlank

data class PlayerGameRequest(

        @NotBlank
        val player_id: UUID,

        @NotBlank
        val game_id: UUID
) {
    fun toPlayerGameDomain(): PlayerGameDomain = PlayerGameDomain(playerId=player_id, gameId=game_id)
}