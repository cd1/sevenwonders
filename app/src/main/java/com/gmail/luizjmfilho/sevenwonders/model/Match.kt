package com.gmail.luizjmfilho.sevenwonders.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateTime: LocalDateTime,
)
