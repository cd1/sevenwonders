package com.gmail.luizjmfilho.sevenwonders.data

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeConverter {
    private val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(dateTimeFormatter)
    }

    @TypeConverter
    fun toLocalDateTime(convertedDateTime: String): LocalDateTime {
        return LocalDateTime.parse(convertedDateTime, dateTimeFormatter)
    }
}