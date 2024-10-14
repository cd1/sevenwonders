package com.gmail.luizjmfilho.sevenwonders.ui

data class MatchesHistoryUiState(
    val matches: List<Match> = emptyList(),
) {
    data class Match(
        val matchId: Int,
        val dateTime: String,
        val players: List<Player>,
    ) {
        data class Player(
            val name: String,
            val position: Int,
            val totalScore: Int,
            val wonder: Wonders,
            val wonderSide: WonderSide,
            val wonderBoardScore: Int,
            val coinScore: Int,
            val warScore: Int,
            val blueCardScore: Int,
            val yellowCardScore: Int,
            val greenCardScore: Int,
            val purpleCardScore: Int,
        )
    }
}

enum class VisualizationMode {
    GeneralInfo,
    DetailsInfo,
}