package com.rmulka.bs.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.rmulka.bs.domain.Game
import com.rmulka.bs.domain.GameDetails
import org.jooq.JSONB
import org.springframework.stereotype.Component
import com.rmulka.bs.jooq.generated.tables.pojos.Game as GamePojo

@Component
class ConverterUtil(private val objectMapper: ObjectMapper) {

    fun toGameDomain(game: GamePojo): Game =
            Game(
                    id = game.id,
                    inProgress = game.inProgress,
                    details = objectMapper.readValue(game.details.toString(), GameDetails::class.java),
                    numPlayers = game.numPlayers,
                    creatorName = game.creatorName,
                    createdAt = game.createdAt,
                    updatedAt = game.updatedAt,
                    isActive = game.isActive,
                    creatorId = game.creatorId
            )

    fun toGameDetails(details: JSONB): GameDetails =
            objectMapper.readValue(details.toString(), GameDetails::class.java)
}