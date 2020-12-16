package com.rmulka.bs.repository

import com.rmulka.bs.domain.PlayerGameDomain
import com.rmulka.bs.jooq.generated.tables.daos.PlayerGameDao
import com.rmulka.bs.jooq.generated.tables.pojos.PlayerGame
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlayerGameDao(private val dslContext: DSLContext) : PlayerGameDao(dslContext.configuration()) {

    suspend fun joinGame(playerGameDomain: PlayerGameDomain, creator: Boolean = false) {
        this.insert(PlayerGame(
                UUID.randomUUID(),
                playerGameDomain.playerId,
                playerGameDomain.gameId,
                creator
        ))
    }
}