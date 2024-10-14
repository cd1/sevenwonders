package com.gmail.luizjmfilho.sevenwonders.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.gmail.luizjmfilho.sevenwonders.data.PlayersListRepository
import com.gmail.luizjmfilho.sevenwonders.model.Player
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersListViewModel @Inject constructor(
    private val playersListRepository: PlayersListRepository,
    savedStateHandle: SavedStateHandle,
    firebaseAnalytics: FirebaseAnalytics,
) : TrackedScreenViewModel(firebaseAnalytics, "PlayersList")  {


    private val alreadySelectedPlayerIds = savedStateHandle.get<String>("alreadySelectedPlayerIds")?.split(",")?.map { it.toInt() } ?: emptyList()
    private val _uiState = MutableStateFlow(PlayersListUiState())
    val uiState: StateFlow<PlayersListUiState> = _uiState.asStateFlow()

    private var players = emptyList<Player>()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                filterPlayersByAlreadySelectedPlayerIds()
                currentState.copy(
                    playerNames = players.map { it.name },
                )
            }
        }
    }

    private suspend fun filterPlayersByAlreadySelectedPlayerIds() {
        players = playersListRepository.readPlayer().filter { it.id !in alreadySelectedPlayerIds }
    }

    fun onNewPlayerConfirmClick() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val addPlayerResult = playersListRepository.addPlayer(currentState.newPlayerName.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })
                if (addPlayerResult == null) {
                    filterPlayersByAlreadySelectedPlayerIds()
                    currentState.copy(
                        newPlayerName = "",
                        playerNames = players.map { it.name },
                        newPlayerNameError = null,
                        isNewPlayerDialogShown = false
                    )
                } else {
                    currentState.copy(
                        newPlayerNameError = addPlayerResult.nameError,
                        isNewPlayerDialogShown = true
                    )
                }
            }
        }
    }

    fun onTypeNewPlayer(playerName: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val newName = if (playerName.length <= 10) playerName else currentState.newPlayerName
                currentState.copy(
                    newPlayerName = newName,
                )
            }
        }
    }

    fun onNewPlayerFloatingButtonClick() {
        _uiState.update {  currentState ->
            currentState.copy(
                isNewPlayerDialogShown = true,
                newPlayerName = ""
            )
        }
    }

    fun onNewPlayerDismiss() {
        _uiState.update {  currentState ->
            currentState.copy(
                isNewPlayerDialogShown = false
            )
        }
    }

    fun onDeletePlayer(playerIndex: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                playersListRepository.deletePlayer(players[playerIndex].id)
                filterPlayersByAlreadySelectedPlayerIds()
                currentState.copy(
                    playerNames = players.map { it.name },
                )
            }
        }
    }

    fun getPlayerIdFromIndex(index: Int): Int {
        return players[index].id
    }

}

