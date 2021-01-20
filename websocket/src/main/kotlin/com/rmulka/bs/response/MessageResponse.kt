package com.rmulka.bs.response

import java.util.UUID

data class MessageResponse(
        val messages: List<Pair<UUID, String>>
)