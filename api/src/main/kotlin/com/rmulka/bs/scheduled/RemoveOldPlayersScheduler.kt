package com.rmulka.bs.scheduled

import com.rmulka.bs.repository.PlayerDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@EnableScheduling
class RemoveOldPlayersScheduler(private val playerDao: PlayerDao) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Scheduled(cron = "0 0/20 * * * *", zone = "GMT-6")
    fun removeStaleGames() {
        GlobalScope.launch {
            playerDao.removeOldPlayers(LocalDateTime.now().minusDays(1)).also {
                logger.info("Executed scheduled old players removal...removed $it players")
            }
        }
    }
}