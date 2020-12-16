package com.rmulka.bs.repository

import com.rmulka.bs.jooq.generated.tables.daos.PlayerDao
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PlayerDao(private val dslContext: DSLContext) : PlayerDao(dslContext.configuration())