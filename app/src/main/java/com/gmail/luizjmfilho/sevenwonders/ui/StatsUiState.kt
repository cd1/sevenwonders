package com.gmail.luizjmfilho.sevenwonders.ui

data class StatsUiState(
    val isLoading: Boolean = true,
    val isDatabaseEmpty: Boolean = true,
    val bestScoresList: PlayersWithSameScore = PlayersWithSameScore(0, emptyList()),
    val worstScoresList: PlayersWithSameScore = PlayersWithSameScore(0, emptyList()),
    val bestScoresPerPlayerList: List<ScorePerPlayer> = emptyList(),
    val worstScoresPerPlayerList: List<ScorePerPlayer> = emptyList(),
    val averageWinnerScore: Int = 0,
    val averageScorePerPlayer: List<Pair<String, Int>> = emptyList(),
    val mostAbsoluteChampionList: List<Pair<String, Int>> = emptyList(),
    val mostRelativeChampionList: List<Pair<String, Int>> = emptyList(),
    val allAbsoluteVictoriesList: List<Pair<String, Int>> = emptyList(),
    val allRelativeVictoriesList: List<Pair<String, Int>> = emptyList(),
    val blueList: List<Pair<String, Int>> = emptyList(),
    val yellowList: List<Pair<String, Int>> = emptyList(),
    val greenList: List<Pair<String, Int>> = emptyList(),
    val purpleList: List<Pair<String, Int>> = emptyList(),
    val bestWondersList: List<FrequencyPerWonder> = emptyList(),
)

data class PlayersWithSameScore(
    val score: Int,
    val players: List<Player>,
) {
    data class Player(
        val name: String,
        val wonder: Wonders,
        val wonderSide: WonderSide,
    )
}

data class ScorePerPlayer(
    val playerName: String,
    val score: Int,
    // TODO: only one Wonder and WonderSide per player? What if there is more than one wonder per player with the same score?
    val wonder: Wonders,
    val wonderSide: WonderSide,
)

data class ResultadoDaConsultaSQLAverageScorePerPlayer(
    val name: String,
    val score: Int,
)

data class FrequencyPerWonder(
    val wonder: Wonders,
    val wonderSide: WonderSide,
    val times: Int,
)