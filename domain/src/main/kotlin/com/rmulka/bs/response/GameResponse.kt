package com.rmulka.bs.response

import com.rmulka.bs.jooq.generated.tables.pojos.Game
import com.rmulka.bs.domain.GameDetails
import com.rmulka.bs.domain.Player
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class GameResponse(
        val id: UUID,
        val inProgress: Boolean,
        val details: GameDetails,
        val numPlayers: Int,
        val creatorName: String,
        val isActive: Boolean,
        val creatorId: UUID,
        val players: List<Player>,
        val timerStart: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
) {
    constructor(game: Game, details: GameDetails, players: List<Player>): this(
            id = game.id,
            inProgress = game.inProgress,
            details = details,
            numPlayers = game.numPlayers,
            creatorName = game.creatorName,
            isActive = game.isActive,
            creatorId = game.creatorId,
            players = players,
            timerStart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    )
}