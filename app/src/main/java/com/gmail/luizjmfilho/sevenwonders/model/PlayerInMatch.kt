package com.gmail.luizjmfilho.sevenwonders.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gmail.luizjmfilho.sevenwonders.ui.WonderSide
import com.gmail.luizjmfilho.sevenwonders.ui.Wonders

@Entity(
    foreignKeys = [
        ForeignKey(entity = Player::class, parentColumns = ["id"], childColumns = ["playerId"], onDelete = ForeignKey.RESTRICT),
        ForeignKey(entity = Match::class, parentColumns = ["id"], childColumns = ["matchId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [
        Index(value = ["playerId"]),
        Index(value = ["matchId"]),
    ],
)
data class PlayerInMatch(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playerId: Int,
    val matchId: Int,
    val wonder: Wonders,
    val wonderSide: WonderSide,
    val totalScore: Int, // TODO: can we remove this field?
    val wonderBoardScore: Int,
    val coinScore: Int, // TODO: can we remove this field?
    val coinCount: Int,
    val warScore: Int,
    val blueCardScore: Int,
    val yellowCardScore: Int,
    val greenCardScore: Int,
    val purpleCardScore: Int,
    val position: Int, // TODO: can we remove this field?
)