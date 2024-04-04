package com.example.insulindiary.persistence

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    @TypeConverter
    fun fromLocalTime(localTime: LocalTime): Int = localTime.toSecondOfDay()

    @TypeConverter
    fun toLocalTime(secondOfDay: Int): LocalTime = LocalTime.ofSecondOfDay(secondOfDay.toLong())

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate): Long = localDate.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)
}