package com.rmulka.bs.repository

import com.rmulka.bs.domain.PlayerGameDomain
import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.jooq.generated.Tables
import com.rmulka.bs.jooq.generated.tables.daos.PlayerGameDao
import com.rmulka.bs.jooq.generated.tables.pojos.Game
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

    suspend fun fetchByPlayerId(playerId: UUID): PlayerGame = dslContext
            .select(*Tables.PLAYER_GAME.fields())
            .from(Tables.PLAYER_GAME)
            .innerJoin(Tables.GAME).on(Tables.PLAYER_GAME.GAME_ID.eq(Tables.GAME.ID).and(Tables.GAME.IS_ACTIVE.eq(true)))
            .where(Tables.PLAYER_GAME.PLAYER_ID.eq(playerId))
            .fetchOneInto(PlayerGame::class.java)
            ?: throw ResourceNotFoundException("Player $playerId was not found")

    suspend fun leaveGame(playerId: UUID) = dslContext
            .deleteFrom(Tables.PLAYER_GAME)
            .where(Tables.PLAYER_GAME.PLAYER_ID.eq(playerId))
            .execute()

    suspend fun deletePlayerGame(gameId: UUID) {
        dslContext
                .deleteFrom(Tables.PLAYER_GAME)
                .where(Tables.PLAYER_GAME.GAME_ID.eq(gameId))
                .execute()
    }

    suspend fun fetchGameByPlayerId(playerId: UUID): Game = dslContext
            .select(*Tables.GAME.fields())
            .from(Tables.PLAYER_GAME)
            .innerJoin(Tables.GAME).on(Tables.PLAYER_GAME.GAME_ID.eq(Tables.GAME.ID).and(Tables.GAME.IS_ACTIVE.eq(true)))
            .where(Tables.PLAYER_GAME.PLAYER_ID.eq(playerId))
            .fetchOneInto(Game::class.java)
            ?: throw ResourceNotFoundException("Player $playerId not in a game")
}