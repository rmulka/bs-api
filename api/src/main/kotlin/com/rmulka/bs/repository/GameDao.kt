package com.rmulka.bs.repository

import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.jooq.generated.Tables
import com.rmulka.bs.jooq.generated.tables.pojos.Game
import com.rmulka.bs.jooq.generated.tables.daos.GameDao
import com.rmulka.bs.jooq.generated.tables.pojos.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class GameDao(private val dslContext: DSLContext) : GameDao(dslContext.configuration()) {

    suspend fun fetchAllGames(): List<Game> = dslContext
            .selectFrom(Tables.GAME)
            .where(Tables.GAME.NUM_PLAYERS.greaterThan(0).and(Tables.GAME.IS_ACTIVE.eq(true)))
            .fetchInto(Game::class.java)

    suspend fun fetchGame(id: UUID): Game =
            this.fetchOneById(id) ?: throw ResourceNotFoundException("Game $id not found")

    suspend fun addPlayer(gameId: UUID) {
        this.fetchOneById(gameId)?.let {
            it.numPlayers++
            it.updatedAt = LocalDateTime.now()
            this.update(it)
        } ?: throw ResourceNotFoundException("Game id $gameId does not exist")
    }

    suspend fun removePlayer(gameId: UUID) {
        this.fetchOneById(gameId)?.let {
            it.numPlayers--
            it.updatedAt = LocalDateTime.now()
            this.update(it)
        } ?: throw ResourceNotFoundException("Game id $gameId does not exist")
    }

    suspend fun createGame(player: Player, gameJson: JSONB): UUID =
        Game(
                UUID.randomUUID(),
                false,
                gameJson,
                0,
                player.playerName,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                player.id,
                1
        ).let {
            this.insert(it)
            it.id
        }

    suspend fun deleteGame(gameId: UUID) {
        this.fetchOneById(gameId)?.let {
            it.isActive = false
            it.updatedAt = LocalDateTime.now()
            this.update(it)
        } ?: throw ResourceNotFoundException("Game id $gameId does not exist")
    }

    private suspend fun fetchStaleGameIds(now: LocalDateTime): List<UUID> = dslContext
            .select()
            .from(Tables.GAME)
            .where(Tables.GAME.UPDATED_AT.lessThan(now.minusMinutes(10)).and(Tables.GAME.IS_ACTIVE.eq(true)))
            .fetch(Tables.GAME.ID, UUID::class.java)

    suspend fun removeStaleGames(now: LocalDateTime): List<UUID> = withContext(Dispatchers.IO) {
        fetchStaleGameIds(now).also {
            dslContext
                    .update(Tables.GAME)
                    .set(Tables.GAME.IS_ACTIVE, false)
                    .where(Tables.GAME.UPDATED_AT.lessThan(now.minusMinutes(10)).and(Tables.GAME.IS_ACTIVE.eq(true)))
                    .execute()

            dslContext
                    .deleteFrom(Tables.PLAYER_GAME)
                    .where(Tables.PLAYER_GAME.GAME_ID.`in`(it))
                    .execute()
        }
    }

    suspend fun deleteOldGames(time: LocalDateTime): Int = withContext(Dispatchers.IO) {
        dslContext
                .deleteFrom(Tables.GAME)
                .where(Tables.GAME.CREATED_AT.lt(time).and(Tables.GAME.IS_ACTIVE.eq(false)))
                .execute()
    }
}