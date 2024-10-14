package com.gmail.luizjmfilho.sevenwonders.data

import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import com.gmail.luizjmfilho.sevenwonders.model.Player
import com.gmail.luizjmfilho.sevenwonders.ui.WonderSide
import com.gmail.luizjmfilho.sevenwonders.ui.Wonders
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.Month
import java.util.Calendar

class SevenWondersDatabaseMigrationTest {
    @get:Rule
    val helper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), SevenWondersDatabase::class.java)

    @Test
    fun migrate4To5() {
        val players = listOf(
            Player(4, "Deivinho"),
            Player(8, "Zinho"),
            Player(15, "Lulu"),
            Player(16, "Lalá"),
        )

        val matchId = 42
        val matchDateTime = LocalDateTime.of(2024, Month.OCTOBER, 16, 10, 20)
        val utilDate = Calendar.getInstance().run {
            set(Calendar.YEAR, matchDateTime.year)
            set(Calendar.MONTH, matchDateTime.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, matchDateTime.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, matchDateTime.hour)
            set(Calendar.MINUTE, matchDateTime.minute)

            time
        }
        val stringMatchDatePart = DateFormat.getDateInstance(DateFormat.SHORT).format(utilDate)
        val stringMatchTimePart = DateFormat.getTimeInstance(DateFormat.SHORT).format(utilDate)
        val matchDateTimeString = "$stringMatchDatePart - $stringMatchTimePart"

        helper.createDatabase(DB_NAME, 4).use { db ->
            for (player in players) {
                db.insert("Person", SQLiteDatabase.CONFLICT_FAIL, contentValuesOf(
                    "id" to player.id,
                    "name" to player.name,
                ))
            }

            db.insert("Match", SQLiteDatabase.CONFLICT_FAIL, contentValuesOf(
                "matchId" to matchId,
                "dataAndTime" to matchDateTimeString,
                "position" to 1,
                "nickname" to players[0].name,
                "wonder" to Wonders.GIZAH.name,
                "wonderSide" to WonderSide.Day.name,
                "totalScore" to 99,
                "wonderBoardScore" to 10,
                "coinScore" to 20,
                "coinQuantity" to 21,
                "warScore" to 30,
                "blueCardScore" to 40,
                "yellowCardScore" to 50,
                "greenCardScore" to 60,
                "purpleCardScore" to 70,
            ))

            db.insert("Match", SQLiteDatabase.CONFLICT_FAIL, contentValuesOf(
                "matchId" to matchId,
                "dataAndTime" to matchDateTimeString,
                "position" to 2,
                "nickname" to players[1].name,
                "wonder" to Wonders.ALEXANDRIA.name,
                "wonderSide" to WonderSide.Night.name,
                "totalScore" to 9,
                "wonderBoardScore" to 1,
                "coinScore" to 2,
                "coinQuantity" to 2,
                "warScore" to 3,
                "blueCardScore" to 4,
                "yellowCardScore" to 5,
                "greenCardScore" to 6,
                "purpleCardScore" to 7,
            ))

            db.insert("Match", SQLiteDatabase.CONFLICT_FAIL, contentValuesOf(
                "matchId" to matchId,
                "dataAndTime" to matchDateTimeString,
                "position" to 4,
                "nickname" to players[2].name,
                "wonder" to Wonders.BABYLON.name,
                "wonderSide" to WonderSide.Day.name,
                "totalScore" to 7,
                "wonderBoardScore" to 0,
                "coinScore" to 0,
                "coinQuantity" to 0,
                "warScore" to 1,
                "blueCardScore" to 2,
                "yellowCardScore" to 3,
                "greenCardScore" to 4,
                "purpleCardScore" to 5,
            ))

            db.insert("Match", SQLiteDatabase.CONFLICT_FAIL, contentValuesOf(
                "matchId" to matchId,
                "dataAndTime" to matchDateTimeString,
                "position" to 3,
                "nickname" to players[3].name,
                "wonder" to Wonders.RHODOS.name,
                "wonderSide" to WonderSide.Night.name,
                "totalScore" to 8,
                "wonderBoardScore" to 0,
                "coinScore" to 1,
                "coinQuantity" to 1,
                "warScore" to 2,
                "blueCardScore" to 3,
                "yellowCardScore" to 4,
                "greenCardScore" to 5,
                "purpleCardScore" to 6,
            ))
        }

        helper.runMigrationsAndValidate(DB_NAME, 5, true, Migration4To5).use { db ->
            db.query("SELECT id, name FROM Player ORDER BY id").use { cursor ->
                assertEquals("Player count doesn't match", players.size, cursor.count)

                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(0)
                        val name = cursor.getString(1)

                        val player = players.find { it.id == id }

                        assertNotNull("A player with ID=$id was found, but that ID wasn't inserted before", player)
                        assertEquals("Player names don't match", player!!.name, name)
                    } while (cursor.moveToNext())
                }
            }

            db.query("SELECT id, dateTime FROM Match ORDER BY id").use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(0)
                        val dateTime = cursor.getString(1)

                        assertEquals("Match ID doesn't match", matchId, id)
                        assertEquals("Match date/time doesn't match", matchDateTime, DateTimeConverter.toLocalDateTime(dateTime))
                    } while (cursor.moveToNext())
                } else {
                    fail("Could not find any Match")
                }
            }

            // TODO: Check PlayerInMatch
        }
    }

    companion object {
        private const val DB_NAME = "test.db"
    }
}