package com.gmail.luizjmfilho.sevenwonders

import android.content.Context
import androidx.room.Room
import com.gmail.luizjmfilho.sevenwonders.data.Migration4To5
import com.gmail.luizjmfilho.sevenwonders.data.MatchDao
import com.gmail.luizjmfilho.sevenwonders.data.PlayerDao
import com.gmail.luizjmfilho.sevenwonders.data.SevenWondersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun createDatabase(@ApplicationContext context: Context): SevenWondersDatabase {
        return Room
            .databaseBuilder(context, SevenWondersDatabase::class.java, "SevenWonders.db")
            .addMigrations(Migration4To5)
            .build()
    }

    @Provides
    fun createDao(database: SevenWondersDatabase): PlayerDao {
        return database.playerDao()
    }

    @Provides
    fun matchDao(database: SevenWondersDatabase): MatchDao {
        return database.matchDao()
    }
}


