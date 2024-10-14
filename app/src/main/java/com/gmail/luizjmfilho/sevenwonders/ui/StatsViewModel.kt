package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.lifecycle.viewModelScope
import com.gmail.luizjmfilho.sevenwonders.data.PlayerScore
import com.gmail.luizjmfilho.sevenwonders.data.StatsRepository
import com.gmail.luizjmfilho.sevenwonders.firebasePerformanceTrace
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsRepository: StatsRepository,
    firebaseAnalytics: FirebaseAnalytics,
): TrackedScreenViewModel(firebaseAnalytics, "Stats") {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            firebasePerformanceTrace("stats_calculation") { trace ->
                val allMatches =  statsRepository.selectAllMatches()

                _uiState.update { currentState ->
                    val bestScoreList = statsRepository.getBestScoreList().toPlayersWithSameScore()
                    val worstScoreList = statsRepository.getWorstScoreList().toPlayersWithSameScore()
                    val bestScoresPerPlayerList = statsRepository.getBestScoresPerPlayerList().sortedByDescending { it.totalScore }.map { it.toScorePerPlayer() }
                    val worstScoresPerPlayerList = statsRepository.getWorstScoresPerPlayerList().sortedBy { it.totalScore }.map { it.toScorePerPlayer() }
                    val averageWinnerScore = statsRepository.getAverageWinnerScore()
                    val averageScorePerPlayer = statsRepository.getAverageScorePerPlayer().sortedByDescending { it.second }

                    val numberOfVictoriesPerPlayer = statsRepository.selectAllMatches().flatMap { matchWithPlayers -> matchWithPlayers.value.filter { it.position == 1 } }.map { statsRepository.getPlayerNameById(it.playerId) }.groupingBy { it }.eachCount()
                    val mostAbsoluteChampionList = numberOfVictoriesPerPlayer.filter { it.value == numberOfVictoriesPerPlayer.values.maxOrNull() }.toList()
                    val numberOfMatchesPerPlayer = statsRepository.selectAllMatches().flatMap { it.value }.map { statsRepository.getPlayerNameById(it.playerId) }.groupingBy { it }.eachCount()
                    val ratioOfVictoriesPerPlayer = numberOfVictoriesPerPlayer.mapValues { (nickname, contagem) ->
                            val totalMatches = numberOfMatchesPerPlayer.get(nickname) ?: 0
                            if (totalMatches > 0) {
                                (contagem.toDouble() / totalMatches) * 100
                            } else {
                                0.0
                            }
                        }.mapValues { it.value.toInt() }

                    val blueList = statsRepository.getBlueRecordsList().map { it.name to it.score }
                    val yellowList = statsRepository.getYellowRecordsList().map { it.name to it.score }
                    val greenList = statsRepository.getGreenRecordsList().map { it.name to it.score }
                    val purpleList = statsRepository.getPurpleRecordsList().map { it.name to it.score }

                    val bestWondersList = statsRepository.getBestWondersList()

                    currentState.copy(
                        isDatabaseEmpty = allMatches.isEmpty(),
                        bestScoresList = bestScoreList,
                        worstScoresList = worstScoreList,
                        bestScoresPerPlayerList = bestScoresPerPlayerList,
                        worstScoresPerPlayerList = worstScoresPerPlayerList,
                        averageWinnerScore = averageWinnerScore,
                        averageScorePerPlayer = averageScorePerPlayer,
                        mostAbsoluteChampionList = mostAbsoluteChampionList,
                        mostRelativeChampionList = ratioOfVictoriesPerPlayer.filter { it.value == ratioOfVictoriesPerPlayer.values.maxOrNull() }.toList(),
                        allAbsoluteVictoriesList = numberOfVictoriesPerPlayer.toList().sortedByDescending { it.second },
                        allRelativeVictoriesList = ratioOfVictoriesPerPlayer.toList().sortedByDescending { it.second },
                        blueList = blueList,
                        yellowList = yellowList,
                        greenList = greenList,
                        purpleList = purpleList,
                        bestWondersList = bestWondersList,
                        isLoading = false
                    )
                }

                val matchCount = allMatches.size
                trace.putAttribute("match_count", matchCount.toString())
            }
        }
    }

    companion object {
        private fun PlayerScore.toScorePerPlayer(): ScorePerPlayer {
            return ScorePerPlayer(
                playerName = this.name,
                score = this.totalScore,
                wonder = this.wonder,
                wonderSide = this.wonderSide,
            )
        }
        private fun List<PlayerScore>.toPlayersWithSameScore(): PlayersWithSameScore {
            return PlayersWithSameScore(
                score = this.firstOrNull()?.totalScore ?: -1,
                players = this.map { it.toPlayersWithSameScorePlayer() },
            )
        }

        private fun PlayerScore.toPlayersWithSameScorePlayer(): PlayersWithSameScore.Player {
            return PlayersWithSameScore.Player(
                name = this.name,
                wonder = this.wonder,
                wonderSide = this.wonderSide,
            )
        }
    }
}