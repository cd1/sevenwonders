package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.lifecycle.viewModelScope
import com.gmail.luizjmfilho.sevenwonders.data.MatchesHistoryRepository
import com.gmail.luizjmfilho.sevenwonders.model.Match
import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class MatchesHistoryViewModel @Inject constructor(
    private val matchesHistoryRepository: MatchesHistoryRepository,
    firebaseAnalytics: FirebaseAnalytics,
) : TrackedScreenViewModel(firebaseAnalytics, "MatchesHistory") {

    private val _uiState = MutableStateFlow(MatchesHistoryUiState())
    val uiState: StateFlow<MatchesHistoryUiState> = _uiState.asStateFlow()

    private val shortDateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    matches = matchesHistoryRepository.selectAllMatches().map { it.toUiState() },
                )
            }
        }
    }

    fun onDeleteMatchById(id: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                matchesHistoryRepository.deleteMatchById(id)
                currentState.copy(
                    matches = matchesHistoryRepository.selectAllMatches().map { it.toUiState() },
                )
            }
        }
    }

    private suspend fun Map.Entry<Match, List<PlayerInMatch>>.toUiState(): MatchesHistoryUiState.Match {
        return MatchesHistoryUiState.Match(
            matchId = key.id,
            dateTime = shortDateTimeFormatter.format(key.dateTime),
            players = value.sortedBy { it.position }.map { it.toUiState() },
        )
    }

    private suspend fun PlayerInMatch.toUiState(): MatchesHistoryUiState.Match.Player {
        return MatchesHistoryUiState.Match.Player(
            name = matchesHistoryRepository.getPlayerNameById(this.playerId),
            position = this.position,
            totalScore = this.totalScore,
            wonder = this.wonder,
            wonderSide = this.wonderSide,
            wonderBoardScore = this.wonderBoardScore,
            coinScore = this.coinScore,
            warScore = this.warScore,
            blueCardScore = this.blueCardScore,
            yellowCardScore = this.yellowCardScore,
            greenCardScore = this.greenCardScore,
            purpleCardScore = this.purpleCardScore,
        )
    }
}