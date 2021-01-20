package com.rmulka.bs.repository

import com.rmulka.bs.jooq.generated.Tables
import com.rmulka.bs.jooq.generated.tables.daos.ChatDao
import com.rmulka.bs.jooq.generated.tables.pojos.Chat
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class ChatDao(private val dslContext: DSLContext) : ChatDao(dslContext.configuration()) {

    fun insertMessage(gameId: UUID, playerId: UUID, message: String): Chat =
        Chat(
                UUID.randomUUID(),
                gameId,
                playerId,
                message,
                LocalDateTime.now(),
                1
        ).let {
            this.insert(it)
            it
        }

    fun fetchMessages(gameId: UUID): List<Chat> =
            dslContext
                    .selectFrom(Tables.CHAT)
                    .where(Tables.CHAT.GAME_ID.eq(gameId))
                    .orderBy(Tables.CHAT.CREATED_AT.desc())
                    .fetchInto(Chat::class.java)
}