package com.rmulka.bs.repository

import com.rmulka.bs.jooq.generated.Tables
import com.rmulka.bs.jooq.generated.tables.daos.PlayerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class PlayerDao(private val dslContext: DSLContext) : PlayerDao(dslContext.configuration()) {

    suspend fun removeOldPlayers(time: LocalDateTime): Int = withContext(Dispatchers.IO) {
        dslContext
                .deleteFrom(Tables.PLAYER)
                .where(Tables.PLAYER.CREATED_AT.lt(time))
                .execute()
    }
}