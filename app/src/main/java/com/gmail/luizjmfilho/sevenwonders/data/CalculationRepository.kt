package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.model.Match
import com.gmail.luizjmfilho.sevenwonders.model.Player
import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch
import javax.inject.Inject

class CalculationRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao,
) {
    suspend fun addMatch(match: Match, playersInMatch: Set<PlayerInMatch>): Int {
        return matchDao.insertMatch(match, playersInMatch)
    }

    suspend fun getPlayersFromIds(playerIds: List<Int>): List<Player> {
        return playerDao.select(playerIds)
    }
}