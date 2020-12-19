package com.rmulka.bs.repository

import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.jooq.generated.Tables
import com.rmulka.bs.jooq.generated.tables.pojos.Game
import com.rmulka.bs.jooq.generated.tables.daos.GameDao
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class GameDao(private val dslContext: DSLContext) : GameDao(dslContext.configuration()) {

    suspend fun getNumPlayersInGame(gameId: UUID): Int = dslContext
            .select(Tables.GAME.NUM_PLAYERS)
            .from(Tables.GAME)
            .where(Tables.GAME.ID.eq(gameId))
            .fetchOneInto(Int::class.java) ?: throw ResourceNotFoundException("Game id $gameId does not exist")

    suspend fun fetchAllGames(): List<Game> = dslContext
            .selectFrom(Tables.GAME)
            .fetchInto(Game::class.java)

    suspend fun fetchGame(id: UUID): Game =
            this.fetchOneById(id)

    suspend fun addPlayer(gameId: UUID) {
        this.fetchOneById(gameId)?.let {
            it.numPlayers++
            it.updatedAt = LocalDateTime.now()
            this.update(it)
        } ?: throw ResourceNotFoundException("Game id $gameId does not exist")
    }

    suspend fun createGame(userName: String, gameJson: JSONB): UUID =
        Game(
                UUID.randomUUID(),
                false,
                gameJson,
                0,
                userName,
                LocalDateTime.now(),
                LocalDateTime.now()
        ).let {
            this.insert(it)
            it.id
        }
}