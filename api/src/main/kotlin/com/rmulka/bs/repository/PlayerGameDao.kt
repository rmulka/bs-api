package com.rmulka.bs.repository

import com.rmulka.bs.domain.PlayerGameDomain
import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.jooq.generated.Tables
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

    suspend fun fetchByPlayerId(playerId: UUID): PlayerGame =
            this.fetchOne(Tables.PLAYER_GAME.PLAYER_ID, playerId)
                    ?: throw ResourceNotFoundException("Player $playerId was not found")

    suspend fun leaveGame(playerId: UUID) = dslContext
            .deleteFrom(Tables.PLAYER_GAME)
            .where(Tables.PLAYER_GAME.PLAYER_ID.eq(playerId))
            .execute()
}