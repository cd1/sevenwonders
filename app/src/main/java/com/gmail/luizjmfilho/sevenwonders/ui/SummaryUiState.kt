package com.gmail.luizjmfilho.sevenwonders.ui

data class SummaryUiState(
    val playerInfos: List<PlayerInfo> = emptyList(),
) {
    data class PlayerInfo(
        val name: String,
        val wonder: Wonders,
        val wonderSide: WonderSide,
        val totalScore: Int,
        val position: Int,
    )
}