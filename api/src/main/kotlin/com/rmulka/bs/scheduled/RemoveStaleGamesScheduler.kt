package com.rmulka.bs.scheduled

import com.rmulka.bs.repository.GameDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@EnableScheduling
class RemoveStaleGamesScheduler(private val gameDao: GameDao) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Scheduled(cron = "0 0/10 * * * *", zone = "GMT-6")
    fun removeStaleGames() {
        GlobalScope.launch {
            gameDao.removeStaleGames(LocalDateTime.now()).also {
                logger.info("Executed scheduled stale game removal...removed ${it.size} stale games")
            }
        }
    }

    @Scheduled(cron = "0 0/20 * * * *", zone = "GMT-6")
    fun deleteOldGames() {
        GlobalScope.launch {
            gameDao.deleteOldGames(LocalDateTime.now().minusDays(1)).also {
                logger.info("Executed scheduled old game deletion...deleted $it games")
            }
        }
    }
}