package com.gmail.luizjmfilho.sevenwonders.data

import com.gmail.luizjmfilho.sevenwonders.model.Player
import com.gmail.luizjmfilho.sevenwonders.ui.NameOrNicknameError
import javax.inject.Inject

data class AddPlayerResult(
    val nameError: NameOrNicknameError?,
)

class PlayersListRepository @Inject constructor (private val playerDao: PlayerDao) {

    suspend fun addPlayer(playerName: String): AddPlayerResult? {

        val nameWithoutSpace = playerName.trim()
        val nameError: NameOrNicknameError? = if (nameWithoutSpace == "") {
            NameOrNicknameError.Empty
        } else if (playerDao.selectNameAlreadyExists(nameWithoutSpace)) {
            NameOrNicknameError.Exists
        } else {
            null
        }

        if (nameError == null) {
            playerDao.insert(Player(name = nameWithoutSpace))
            return null
        } else {
            return AddPlayerResult(
                nameError = nameError
            )
        }
    }

    suspend fun readPlayer(): List<Player> {
        return playerDao.selectAll()
    }

    suspend fun deletePlayer(playerId: Int) {
        playerDao.delete(playerId)
    }
}