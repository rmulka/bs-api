package com.rmulka.bs.service

import com.rmulka.bs.domain.PlayerDomain
import com.rmulka.bs.jooq.generated.tables.pojos.Player
import com.rmulka.bs.repository.PlayerDao
import com.rmulka.bs.response.PlayerResponse
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlayerService(private val playerDao: PlayerDao) {

    suspend fun createPlayer(playerDomain: PlayerDomain): PlayerResponse =
            Player(UUID.randomUUID(), playerDomain.name).let { player ->
                playerDao.insert(player)
                PlayerResponse(player)
            }
}