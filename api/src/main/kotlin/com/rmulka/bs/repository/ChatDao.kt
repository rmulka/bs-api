package com.rmulka.bs.repository

import com.rmulka.bs.jooq.generated.Tables
import com.rmulka.bs.jooq.generated.tables.daos.ChatDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class ChatDao(private val dslContext: DSLContext) : ChatDao(dslContext.configuration()) {

    suspend fun removeOldMessages(time: LocalDateTime): Int = withContext(Dispatchers.IO) {
        dslContext
                .deleteFrom(Tables.CHAT)
                .where(Tables.CHAT.CREATED_AT.lt(time))
                .execute()
    }

    suspend fun removeGameMessages(gameId: UUID): Int = withContext(Dispatchers.IO) {
        dslContext
                .deleteFrom(Tables.CHAT)
                .where(Tables.CHAT.GAME_ID.eq(gameId))
                .execute()
    }
}