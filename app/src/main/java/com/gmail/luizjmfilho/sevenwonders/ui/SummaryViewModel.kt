package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.gmail.luizjmfilho.sevenwonders.data.SummaryRepository
import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val summaryRepository: SummaryRepository,
    firebaseAnalytics: FirebaseAnalytics,
) : TrackedScreenViewModel(firebaseAnalytics, "Summary") {

    private val matchId = savedStateHandle.get<String>("matchId")!!.toInt()

    private val _uiState = MutableStateFlow(SummaryUiState())
    val uiState: StateFlow<SummaryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    playerInfos = summaryRepository.getCurrentMatch(matchId)
                        .map { it.toPlayerInfo() }
                )
            }
        }
    }

    private suspend fun PlayerInMatch.toPlayerInfo(): SummaryUiState.PlayerInfo {
        return SummaryUiState.PlayerInfo(
            name = summaryRepository.getPlayerNameById(this.playerId),
            wonder = this.wonder,
            wonderSide = this.wonderSide,
            totalScore = this.totalScore,
            position = this.position,
        )
    }
}