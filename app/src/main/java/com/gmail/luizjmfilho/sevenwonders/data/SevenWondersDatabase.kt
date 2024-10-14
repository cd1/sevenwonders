package com.gmail.luizjmfilho.sevenwonders.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gmail.luizjmfilho.sevenwonders.model.Match
import com.gmail.luizjmfilho.sevenwonders.model.Player
import com.gmail.luizjmfilho.sevenwonders.model.PlayerInMatch

@Database(
    entities = [Player::class, PlayerInMatch::class, Match::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 3, to = 4, spec = AutoMigration3To4::class),
    ]
)
@TypeConverters(DateTimeConverter::class)
abstract class SevenWondersDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun matchDao(): MatchDao
}