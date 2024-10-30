package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.gmail.luizjmfilho.sevenwonders.data.MatchHistoryRepository
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
class MatchHistoryViewModel @Inject constructor(
    private val repository: MatchHistoryRepository,
    firebaseAnalytics: FirebaseAnalytics,
) : TrackedScreenViewModel(firebaseAnalytics, "MatchesHistory") {

    private val _uiState = MutableStateFlow(MatchHistoryUiState())
    val uiState: StateFlow<MatchHistoryUiState> = _uiState.asStateFlow()

    private val shortDateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        viewModelScope.launch {
            val matches = repository.getAllMatches().map { it.toUiState() }

            _uiState.update { currentState ->
                currentState.copy(matches = matches)
            }
        }
    }

    fun onDeleteMatch(playerId: Int) {
        viewModelScope.launch {
            repository.deleteMatch(playerId)

            // Refresh the displayed matches after deleting one
            val matches = repository.getAllMatches().map { it.toUiState() }

            _uiState.update { currentState ->
                currentState.copy(matches = matches)
            }
        }
    }

    private suspend fun Map.Entry<Match, List<PlayerInMatch>>.toUiState(): MatchHistoryUiState.Match {
        return MatchHistoryUiState.Match(
            matchId = key.id,
            dateTime = shortDateTimeFormatter.format(key.dateTime),
            players = value.sortedBy { it.position }.map { it.toUiState() },
        )
    }

    private suspend fun PlayerInMatch.toUiState(): MatchHistoryUiState.Match.Player {
        return MatchHistoryUiState.Match.Player(
            name = repository.getPlayerName(this.playerId),
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