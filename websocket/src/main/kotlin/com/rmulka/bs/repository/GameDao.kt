package com.rmulka.bs.repository

import com.rmulka.bs.exception.ResourceNotFoundException
import com.rmulka.bs.jooq.generated.tables.daos.GameDao
import com.rmulka.bs.jooq.generated.tables.pojos.Game
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class GameDao(private val dslContext: DSLContext) : GameDao(dslContext.configuration()) {

    fun fetchGame(id: UUID): Game =
            this.fetchOneById(id) ?: throw ResourceNotFoundException("Game $id not found")
}