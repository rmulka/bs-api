package com.rmulka.bs.request

import com.rmulka.bs.domain.PlayerDomain
import javax.validation.constraints.NotBlank

data class PlayerRequest(

        @field:NotBlank(message = "Missing name")
        val name: String
) {
    fun toPlayerDomain(): PlayerDomain = PlayerDomain(id=null, name=name)
}