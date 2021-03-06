package com.rmulka.bs.utils

import com.rmulka.bs.domain.Player
import com.rmulka.bs.jooq.generated.tables.pojos.Game
import com.rmulka.bs.jooq.generated.tables.pojos.Player as PlayerPojo
import com.rmulka.bs.domain.Game as GameDomain
import com.rmulka.bs.response.BasicGameResponse
import com.rmulka.bs.response.GameResponse

fun List<Game>.toBasicGameResponse(): List<BasicGameResponse> =
        this.map { BasicGameResponse(it.id, it.inProgress, it.numPlayers, it.creatorName) }

fun List<PlayerPojo>.toPlayerDomain(): List<Player> =
        this.map { Player(it.id, it.playerName) }

fun buildGameResponse(game: GameDomain, players: List<PlayerPojo>): GameResponse =
        GameResponse(
                id = game.id,
                inProgress = game.inProgress,
                details = game.details,
                numPlayers = game.numPlayers,
                creatorName = game.creatorName,
                isActive = game.isActive,
                creatorId = game.creatorId,
                players = players.toPlayerDomain()
        )

fun <T> List<T>.loopWithRepeatWhile(condition: () -> Boolean, block: (T) -> Unit) {
    var idx = 0
    while(condition()) {
        block(this[idx])
        idx = (idx + 1) % this.size
    }
}