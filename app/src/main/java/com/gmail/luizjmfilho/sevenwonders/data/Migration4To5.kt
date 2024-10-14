package com.gmail.luizjmfilho.sevenwonders.data

import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.transaction
import com.gmail.luizjmfilho.sevenwonders.Logger
import com.gmail.luizjmfilho.sevenwonders.firebasePerformanceTrace
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import java.text.DateFormat
import java.time.LocalDateTime
import java.util.Calendar

object Migration4To5 : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        firebasePerformanceTrace("room_migration_4_to_5") { trace ->
            // No Hilt here :(
            val logger = Logger(Firebase.crashlytics)

            db.transaction {
                // Rename table "Person" to "Player"
                db.execSQL("ALTER TABLE Person RENAME TO Player")

                // Rename table "Match" to "OldMatch", so we can create the [new] "Match" later
                db.execSQL("ALTER TABLE Match RENAME TO OldMatch")

                // Create table [new] "Match"
                db.execSQL("CREATE TABLE Match (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, dateTime TEXT NOT NULL)")

                // Create table "PlayerInMatch" (and its indices)
                db.execSQL("CREATE TABLE PlayerInMatch (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playerId INTEGER NOT NULL, matchId INTEGER NOT NULL, wonder TEXT NOT NULL, wonderSide TEXT NOT NULL, totalScore INTEGER NOT NULL, wonderBoardScore INTEGER NOT NULL, coinScore INTEGER NOT NULL, coinCount INTEGER NOT NULL, warScore INTEGER NOT NULL, blueCardScore INTEGER NOT NULL, yellowCardScore INTEGER NOT NULL, greenCardScore INTEGER NOT NULL, purpleCardScore INTEGER NOT NULL, position INTEGER NOT NULL, FOREIGN KEY(playerId) REFERENCES Player(id) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(matchId) REFERENCES Match(id) ON UPDATE NO ACTION ON DELETE CASCADE )")
                db.execSQL("CREATE INDEX index_PlayerInMatch_playerId ON PlayerInMatch (playerId)")
                db.execSQL("CREATE INDEX index_PlayerInMatch_matchId ON PlayerInMatch (matchId)")

                // Migrate the matches from "OldMatch" to [new] "Match"
                db.query("SELECT matchId, dataAndTime FROM OldMatch GROUP BY matchId").use { cursor ->
                    if (cursor.moveToFirst()) {
                        do {
                            val matchId = cursor.getInt(0)

                            val dataAndTime = cursor.getString(1)
                            val dateTime = try {
                                convertStringDateToLocalDateTime(dataAndTime)
                            } catch (ex: Exception) {
                                logger.warn("Failed to convert string date time ($dataAndTime) to LocalDateTime: ${ex.message}")
                                continue
                            }

                            val stringDateTime = DateTimeConverter.fromLocalDateTime(dateTime)

                            val insertMatchValues = contentValuesOf(
                                "id" to matchId,
                                "dateTime" to stringDateTime,
                            )
                            val insertedMatchId = db.insert("Match", SQLiteDatabase.CONFLICT_FAIL, insertMatchValues)
                            if (insertedMatchId == -1L) {
                                logger.warn("Failed to insert match ($insertMatchValues)")
                            }

                        } while (cursor.moveToNext())
                    }

                    trace.putAttribute("match_count", cursor.count.toString())
                }

                // Migrate the data from "OldMatch" to "PlayerInMatch"
                db.execSQL("""
                    INSERT INTO PlayerInMatch (playerId, matchId, wonder, wonderSide, totalScore, wonderBoardScore, coinScore, coinCount, warScore, blueCardScore, yellowCardScore, greenCardScore, purpleCardScore, position)
                        SELECT p.id, matchId, wonder, wonderSide, totalScore, wonderBoardScore, coinScore, coinQuantity, warScore, blueCardScore, yellowCardScore, greenCardScore, purpleCardScore, position 
                        FROM OldMatch AS m
                        JOIN Player AS p ON m.nickname = p.name
                """)

                // Remove the table "OldMatch", so only the table [new] "Match" remains
                db.execSQL("DROP TABLE OldMatch")
            }
        }
    }

    private fun convertStringDateToLocalDateTime(stringDate: String): LocalDateTime {
        val parts = stringDate.split(" - ")
        require(parts.size == 2)

        val datePart = parts[0]
        val timePart = parts[1]

        val utilDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(datePart)
        requireNotNull(utilDate)
        val utilTime = DateFormat.getTimeInstance(DateFormat.SHORT).parse(timePart)
        requireNotNull(utilTime)

        val calendarDate = Calendar.getInstance().apply { time = utilDate }
        val calendarTime = Calendar.getInstance().apply { time = utilTime }

        return LocalDateTime.of(
            calendarDate.get(Calendar.YEAR),
            calendarDate.get(Calendar.MONTH) + 1,
            calendarDate.get(Calendar.DAY_OF_MONTH),
            calendarTime.get(Calendar.HOUR_OF_DAY),
            calendarTime.get(Calendar.MINUTE),
            calendarTime.get(Calendar.SECOND),
        )
    }
}