package com.rmulka.bs.request

import java.util.UUID

data class MessageRequest(
        val playerId: UUID,
        val message: String
)