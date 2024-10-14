package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch
import javax.inject.Inject

class SummaryRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao,
) {

    suspend fun getCurrentMatch(matchId: Int): List<PlayerInMatch> {
        return matchDao.selectPlayersInMatch(matchId).sortedBy { it.position }
    }

    suspend fun getPlayerNameById(playerId: Int): String {
        return playerDao.selectNameById(playerId)
    }

}