package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.model.Match
import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch
import javax.inject.Inject

class MatchesHistoryRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao,
) {
    suspend fun selectAllMatches(): Map<Match, List<PlayerInMatch>> {
        return matchDao.selectAllMatches()
    }

    suspend fun deleteMatchById(id: Int) {
        matchDao.deleteMatch(id)
    }

    suspend fun getPlayerNameById(id: Int): String {
        return playerDao.selectNameById(id)
    }
}