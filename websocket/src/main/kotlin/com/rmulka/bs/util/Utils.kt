package com.rmulka.bs.util

import com.rmulka.bs.domain.Game
import com.rmulka.bs.jooq.generated.tables.pojos.Player
import com.rmulka.bs.response.GameResponse

fun List<Player>.toPlayerDomain(): List<com.rmulka.bs.domain.Player> =
        this.map { com.rmulka.bs.domain.Player(it.id, it.playerName) }

fun buildGameResponse(game: Game, players: List<Player>): GameResponse =
        GameResponse(
                id = game.id,
                inProgress = game.inProgress,
                details = game.details,
                numPlayers = game.numPlayers,
                creatorName = game.creatorName,
                createdAt = game.createdAt,
                updatedAt = game.updatedAt,
                isActive = game.isActive,
                creatorId = game.creatorId,
                players = players.toPlayerDomain()
        )