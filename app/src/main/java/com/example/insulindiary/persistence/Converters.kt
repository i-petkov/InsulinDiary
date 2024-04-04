package com.example.insulindiary.persistence

import androidx.room.TypeConverter
import com.example.insulindiary.data.DailyInsulinIntake
import com.example.insulindiary.data.Intake
import org.json.JSONArray
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

    @TypeConverter
    fun fromIntakeList(intakes: List<Intake>) = intakes
        .joinToString(prefix = "[", postfix = "]", separator = ",") {
            it.toJason()
        }

    @TypeConverter
    fun toIntakeList(jsonArrayString: String) = JSONArray(jsonArrayString).let { array ->
        List(array.length()) {
            Intake.fromJsonObject(array.getJSONObject(it))
        }
    }
}