package com.example.insulindiary.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "DailyInsulinIntakes")
class DailyInsulinIntake(
    @PrimaryKey val date: LocalDate,
    val intakes: List<Intake>
)

@Entity(tableName = "BaseInsulinIntakes")
class BaseInsulinIntake(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name: String,
    val intakes: List<Intake>
) {
    fun toDailyInsulinIntake(date: LocalDate): DailyInsulinIntake = DailyInsulinIntake(date, intakes)
}

data class Intake(val time: LocalTime, val dosage: Double, val type: String) {
    companion object {
        const val KEY_TIME = "time"
        const val KEY_DOSAGE = "dosage"
        const val KEY_TYPE = "type"

        fun fromJson(jsonString: String) = fromJsonObject(JSONObject(jsonString))

        fun fromJsonObject(json: JSONObject) =
            Intake(
                LocalTime.ofSecondOfDay(json.getLong(KEY_TIME)),
                json.getDouble(KEY_DOSAGE),
                json.getString(KEY_TYPE)
            )
    }

    fun toJason() = """{ "$KEY_TIME": ${time.toSecondOfDay()}, "$KEY_DOSAGE": $dosage, "$KEY_TYPE": "$type" }""".trimIndent()
}
