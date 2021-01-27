package com.rmulka.bs.scheduled

import com.rmulka.bs.repository.ChatDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@EnableScheduling
class RemoveOldMessagesScheduler(private val chatDao: ChatDao) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Scheduled(cron = "0 0/20 * * * *", zone = "GMT-6")
    fun removeOldMessages() {
        GlobalScope.launch {
            chatDao.removeOldMessages(LocalDateTime.now().minusHours(4)).also {
                logger.info("Executed scheduled old messages removal...removed $it chat messages")
            }
        }
    }
}