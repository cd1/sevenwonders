package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.model.Player
import javax.inject.Inject

class NewGameRepository @Inject constructor(private val playerDao: PlayerDao) {

    suspend fun getPlayerFromId(playerId: Int): Player {
        return playerDao.select(playerId)
    }
}