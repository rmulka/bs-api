package com.rmulka.bs.repository

import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.jooq.generated.Tables
import com.rmulka.bs.jooq.generated.tables.daos.GameDao
import com.rmulka.bs.jooq.generated.tables.pojos.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class GameDao(private val dslContext: DSLContext) : GameDao(dslContext.configuration()) {

    fun fetchGame(id: UUID): Game =
            this.fetchOneById(id) ?: throw ResourceNotFoundException("Game $id not found")

    fun startGame(id: UUID, details: JSONB): Game =
            fetchGame(id).let { game ->
                dslContext.newRecord(Tables.GAME, game).let { record ->
                    record.inProgress = true
                    record.details = details
                    record.updatedAt = LocalDateTime.now()
                    record.update()
                    record.into(Game::class.java)
                }
            }

    fun updateGameDetails(game: Game, details: JSONB): Game =
            dslContext.newRecord(Tables.GAME, game).let { record ->
                record.details = details
                record.updatedAt = LocalDateTime.now()
                record.update()
                record.into(Game::class.java)
            }

    fun gameUpdated(gameId: UUID, time: LocalDateTime) {
        CoroutineScope(Dispatchers.IO).launch {
            fetchGame(gameId).let { game ->
                dslContext.newRecord(Tables.GAME, game).let { record ->
                    record.updatedAt = time
                    record.update()
                }
            }
        }
    }
}