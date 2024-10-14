package com.gmail.luizjmfilho.sevenwonders.ui

data class MatchDetailsUiState(
    val creationMethod: CreationMethod? = null,
    val matchPlayersDetails: List<MatchDetailsViewModel.PlayerDetail> = listOf(),
    val availableWondersList: List<Wonders?> = Wonders.entries,
    val isAdvanceButtonEnabled: Boolean = false,
)

enum class CreationMethod {
    AllRaffle,
    RafflePositionChooseWonder,
    ChoosePositionRaffleWonder,
    AllChoose,
}

enum class RaffleOrChoose {
    Raffle,
    Choose,
}

enum class WonderSide {
    Day,
    Night,
}

enum class Wonders {
    ALEXANDRIA,
    BABYLON,
    EPHESOS,
    GIZAH,
    HALIKARNASSOS,
    OLYMPIA,
    RHODOS,
}