package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.model.Player
import javax.inject.Inject


class MatchDetailsRepository @Inject constructor(private val playerDao: PlayerDao) {

    suspend fun getPlayersFromIds(ids: List<Int>): List<Player> {
        return playerDao.select(ids)
    }

}