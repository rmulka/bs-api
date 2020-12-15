package com.rmulka.bs.service

import com.rmulka.bs.jooq.generated.tables.pojos.Player
import com.rmulka.bs.repository.PlayerDao
import com.rmulka.bs.request.PlayerRequest
import com.rmulka.bs.response.PlayerResponse
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlayerService(private val playerDao: PlayerDao) {

    fun createPlayer(playerRequest: PlayerRequest): PlayerResponse {
        val id = UUID.randomUUID()
        val player = playerDao.insert(Player(id, playerRequest.name))
        return PlayerResponse(id, playerRequest.name)
    }
}