package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.model.Match
import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch
import com.gmail.luizjmfilho.sevenwonders.ui.FrequencyPerWonder
import javax.inject.Inject

class StatsRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao,
) {

    suspend fun getBestScoreList(): List<PlayerScore> {
        return matchDao.selectBestScores()
    }

    suspend fun getWorstScoreList(): List<PlayerScore> {
        return matchDao.selectWorstScores()
    }

    suspend fun getBestScoresPerPlayerList(): List<PlayerScore> {
        return matchDao.selectHighestScorePerPlayer()
    }

    suspend fun getWorstScoresPerPlayerList(): List<PlayerScore> {
        return matchDao.selectLowestScorePerPlayer()
    }

    suspend fun getAverageWinnerScore(): Int {
        return matchDao.selectAverageWinnerScore()
    }

    suspend fun getAverageScorePerPlayer(): List<Pair<String, Int>> {
        return matchDao.selectAverageScorePerPlayer().map { it ->
            it.name to it.score
        }
    }

    suspend fun selectAllMatches(): Map<Match, List<PlayerInMatch>> {
        return matchDao.selectAllMatches()
    }

    suspend fun getBlueRecordsList(): List<PlayerScoreSimple> {
        return matchDao.selectHighestBlueScores()
    }

    suspend fun getYellowRecordsList(): List<PlayerScoreSimple> {
        return matchDao.selectHighestYellowScores()
    }

    suspend fun getGreenRecordsList(): List<PlayerScoreSimple> {
        return matchDao.selectHighestGreenScores()
    }

    suspend fun getPurpleRecordsList(): List<PlayerScoreSimple> {
        return matchDao.selectHighestPurpleScores()
    }

    suspend fun getBestWondersList(): List<FrequencyPerWonder> {
        return matchDao.selectWonderFrequency()
    }

    suspend fun getPlayerNameById(playerId: Int): String {
        return playerDao.selectNameById(playerId)
    }
}