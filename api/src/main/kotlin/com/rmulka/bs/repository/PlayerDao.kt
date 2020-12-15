package com.rmulka.bs.repository

import com.rmulka.bs.jooq.generated.tables.daos.PlayerDao as JooqPlayerDao
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PlayerDao(private val dslContext: DSLContext) : JooqPlayerDao(dslContext.configuration())