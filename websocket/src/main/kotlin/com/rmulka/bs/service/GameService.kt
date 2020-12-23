package com.rmulka.bs.service

import com.rmulka.bs.domain.Game
import com.rmulka.bs.repository.GameDao
import com.rmulka.bs.util.ConverterUtil
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameService(private val gameDao: GameDao,
                  private val converterUtil: ConverterUtil) {

    companion object {
        val logger = KotlinLogging.logger {}
    }

    fun fetchGame(gameId: UUID): Game = converterUtil.toGameDomain(gameDao.fetchGame(gameId))
}