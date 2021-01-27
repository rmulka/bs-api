package com.rmulka.bs.service

import com.rmulka.bs.domain.PlayerDomain
import com.rmulka.bs.jooq.generated.tables.pojos.Player
import com.rmulka.bs.repository.PlayerDao
import com.rmulka.bs.response.PlayerResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class PlayerService(private val playerDao: PlayerDao) {

    companion object {
        val logger = KotlinLogging.logger {}
    }

    suspend fun createPlayer(playerDomain: PlayerDomain): PlayerResponse =
            Player(
                    UUID.randomUUID(),
                    playerDomain.name,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    1
            ).let { player ->
                logger.info("Creating player with id ${player.id} and name ${player.playerName}")
                playerDao.insert(player)
                PlayerResponse(player)
            }
}