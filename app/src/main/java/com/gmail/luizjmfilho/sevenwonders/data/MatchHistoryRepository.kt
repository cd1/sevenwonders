package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.model.Match
import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch
import javax.inject.Inject

class MatchHistoryRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao,
) {
    suspend fun getAllMatches(): Map<Match, List<PlayerInMatch>> {
        return matchDao.selectAllMatches()
    }

    suspend fun deleteMatch(id: Int) {
        matchDao.deleteMatch(id)
    }

    suspend fun getPlayerName(playerId: Int): String {
        return playerDao.selectNameById(playerId)
    }
}