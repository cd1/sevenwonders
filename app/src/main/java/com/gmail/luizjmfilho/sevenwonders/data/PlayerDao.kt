package com.gmail.luizjmfilho.sevenwonders.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gmail.luizjmfilho.sevenwonders.model.Player

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(player: Player)

    @Query("SELECT * FROM Player WHERE id = :id")
    suspend fun select(id: Int): Player

    @Query("SELECT * FROM Player ORDER BY name")
    suspend fun selectAll(): List<Player>

    @Query("SELECT * FROM Player WHERE id IN (:ids)")
    suspend fun select(ids: List<Int>): List<Player>

    @Query("""
        SELECT EXISTS(
            SELECT * FROM Player WHERE UPPER(name) = UPPER(:name)
        )
    """)
    suspend fun selectNameAlreadyExists(name: String): Boolean

    @Query("SELECT name FROM Player WHERE id = :id")
    suspend fun selectNameById(id: Int): String

    @Query("DELETE FROM Player WHERE id = :id")
    suspend fun delete(id: Int)
}