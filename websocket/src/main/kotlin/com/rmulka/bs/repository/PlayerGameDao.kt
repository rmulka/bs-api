package com.rmulka.bs.repository

import com.rmulka.bs.jooq.generated.Tables
import com.rmulka.bs.jooq.generated.tables.daos.PlayerGameDao
import com.rmulka.bs.jooq.generated.tables.pojos.Player
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlayerGameDao(private val dslContext: DSLContext) : PlayerGameDao(dslContext.configuration()) {

    fun fetchPlayersByGameId(gameId: UUID): List<Player> = dslContext
            .select(*Tables.PLAYER.fields())
            .from(Tables.PLAYER_GAME)
            .innerJoin(Tables.PLAYER).on(Tables.PLAYER_GAME.PLAYER_ID.eq(Tables.PLAYER.ID))
            .where(Tables.PLAYER_GAME.GAME_ID.eq(gameId))
            .fetchInto(Player::class.java)
}