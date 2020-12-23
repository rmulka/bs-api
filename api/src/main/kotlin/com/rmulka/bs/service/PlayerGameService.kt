package com.rmulka.bs.service

import com.rmulka.bs.domain.Game
import com.rmulka.bs.domain.PlayerGameDomain
import com.rmulka.bs.repository.PlayerDao
import com.rmulka.bs.repository.GameDao
import com.rmulka.bs.repository.PlayerGameDao
import com.rmulka.bs.util.ConverterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlayerGameService(private val gameDao: GameDao,
                        private val playerDao: PlayerDao,
                        private val playerGameDao: PlayerGameDao,
                        private val converterUtil: ConverterUtil) {

    suspend fun deleteGame(playerGameDomain: PlayerGameDomain): Job =
        CoroutineScope(Dispatchers.IO).let {
            it.launch { gameDao.deleteGame(playerGameDomain.gameId) }
            it.launch { playerGameDao.deletePlayerGame(playerGameDomain.gameId) }
        }

    suspend fun fetchGameByPlayerId(playerId: UUID): Game =
            playerGameDao.fetchGameByPlayerId(playerId).let { converterUtil.toGameDomain(it) }

    suspend fun joinGame(playerGameDomain: PlayerGameDomain, isCreator: Boolean): Job =
            CoroutineScope(Dispatchers.IO).let {
                it.launch { playerGameDao.joinGame(playerGameDomain, isCreator) }
                it.launch { gameDao.addPlayer(playerGameDomain.gameId) }
            }

    suspend fun playerLeaveGame(playerGameDomain: PlayerGameDomain): Job =
            CoroutineScope(Dispatchers.IO).let {
                it.launch { gameDao.removePlayer(playerGameDomain.gameId) }
                it.launch { playerGameDao.leaveGame(playerGameDomain.playerId) }
            }
}