package com.gmail.luizjmfilho.sevenwonders.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.gmail.luizjmfilho.sevenwonders.model.Match
import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch
import com.gmail.luizjmfilho.sevenwonders.ui.ResultadoDaConsultaSQLAverageScorePerPlayer
import com.gmail.luizjmfilho.sevenwonders.ui.FrequencyPerWonder
import com.gmail.luizjmfilho.sevenwonders.ui.Wonders
import com.gmail.luizjmfilho.sevenwonders.ui.WonderSide

@Dao
interface MatchDao {
    @Insert
    suspend fun insert(match: Match): Long

    @Insert
    suspend fun insertPlayerInMatch(playerInMatch: PlayerInMatch)

    @Transaction
    suspend fun insertMatch(match: Match, playersInMatch: Set<PlayerInMatch>): Int {
        val matchId = insert(match).toInt()

        for (playerInMatch in playersInMatch) {
            insertPlayerInMatch(playerInMatch.copy(matchId = matchId))
        }

        return matchId
    }

    @Query("""
        SELECT * 
        FROM `Match` JOIN PlayerInMatch ON `Match`.id = matchId 
        ORDER BY dateTime
    """)
    suspend fun selectAllMatches(): Map<Match, List<PlayerInMatch>>

    @Query("SELECT * FROM PlayerInMatch WHERE matchId = :matchId")
    suspend fun selectPlayersInMatch(matchId: Int): List<PlayerInMatch>

    @Query("""
        SELECT totalScore, name, wonder, wonderSide 
        FROM PlayerInMatch JOIN Player ON playerId = Player.id 
        WHERE totalScore = (
            SELECT MAX(totalScore) FROM PlayerInMatch
        )
        ORDER BY name ASC
    """)
    suspend fun selectBestScores(): List<PlayerScore>

    @Query("""
        SELECT totalScore, name, wonder, wonderSide 
        FROM PlayerInMatch JOIN Player ON playerId = Player.id 
        WHERE totalScore = (
            SELECT MIN(totalScore) FROM PlayerInMatch
        ) 
        ORDER BY name ASC
    """)
    suspend fun selectWorstScores(): List<PlayerScore>

    @Query("""
        SELECT MAX(totalScore) AS totalScore, name, wonder, wonderSide
        FROM PlayerInMatch JOIN Player ON playerId = Player.id
        GROUP BY name 
        ORDER BY totalScore ASC
    """)
    suspend fun selectHighestScorePerPlayer(): List<PlayerScore>

    @Query("""
        SELECT MIN(totalScore) AS totalScore, name, wonder, wonderSide 
        FROM PlayerInMatch JOIN Player ON playerId = Player.id 
        GROUP BY name 
        ORDER BY totalScore DESC
    """)
    suspend fun selectLowestScorePerPlayer(): List<PlayerScore>

    @Query("""
        SELECT CAST(AVG(totalScore) AS INT) 
        FROM PlayerInMatch 
        WHERE position = 1
    """)
    suspend fun selectAverageWinnerScore(): Int

    @Query("""
        SELECT name, CAST(AVG(totalScore) AS INT) AS score 
        FROM PlayerInMatch JOIN Player ON playerId = Player.id
        GROUP BY name
    """)
    suspend fun selectAverageScorePerPlayer(): List<ResultadoDaConsultaSQLAverageScorePerPlayer>

    @Query("""
        SELECT name, blueCardScore AS score
        FROM PlayerInMatch JOIN Player ON playerId = Player.id
        WHERE blueCardScore = (
            SELECT MAX(blueCardScore) FROM PlayerInMatch
        )
    """)
    suspend fun selectHighestBlueScores(): List<PlayerScoreSimple>

    @Query("""
        SELECT name, yellowCardScore AS score
        FROM PlayerInMatch JOIN Player ON playerId = Player.id
        WHERE yellowCardScore = (
            SELECT MAX(yellowCardScore) FROM PlayerInMatch
        )
    """)
    suspend fun selectHighestYellowScores(): List<PlayerScoreSimple>

    @Query("""
        SELECT name, greenCardScore AS score
        FROM PlayerInMatch JOIN Player ON playerId = Player.id 
        WHERE greenCardScore = (
            SELECT MAX(greenCardScore) FROM PlayerInMatch
        )
    """)
    suspend fun selectHighestGreenScores(): List<PlayerScoreSimple>

    @Query("""
        SELECT name, purpleCardScore AS score
        FROM PlayerInMatch JOIN Player ON playerId = Player.id
        WHERE purpleCardScore = (
            SELECT MAX(purpleCardScore) FROM PlayerInMatch
        )
    """)
    suspend fun selectHighestPurpleScores(): List<PlayerScoreSimple>

    @Query("""
        SELECT wonder, wonderSide, COUNT(*) AS times
        FROM PlayerInMatch
        WHERE position = 1
        GROUP BY wonder, wonderSide
        ORDER BY times DESC
    """)
    suspend fun selectWonderFrequency(): List<FrequencyPerWonder>

    @Query("SELECT COUNT(*) FROM `Match`")
    suspend fun selectMatchCount(): Int

    @Query("DELETE FROM `Match` WHERE id = :id")
    suspend fun deleteMatch(id: Int)
}

data class PlayerScore(
    val name: String,
    val totalScore: Int,
    val wonder: Wonders,
    val wonderSide: WonderSide,
)

data class PlayerScoreSimple(
    val name: String,
    val score: Int,
)