package com.example.insulindiary.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import java.lang.IllegalStateException
import java.time.ZonedDateTime

sealed interface DailyAggregation {
    val dateTime: ZonedDateTime
    fun colorCode(): Color
}

val DailyAggregation.dayOfMonth: Int
    get() = dateTime.dayOfMonth

fun convertRange(oldValue:Double, oldMin: Double, oldMax: Double, newMin:Double, newMax: Double): Double {
    return (((oldValue - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin
}

data class DailyMeasurementAggregation(override val dateTime: ZonedDateTime, val measurements: List<Measurement>): DailyAggregation {
    override fun colorCode(): Color = measurements.fold(0.0) { acc, measurement ->
        acc + measurement.value
    }.let {
        it / measurements.size
    }.let {
        if (it < 0) {
            throw IllegalStateException("Negative aggregation") // TODO
        } else if (it <= 4.5) {
            Color(0xFF007FFF)
        } else if (it > 13.5) {
            Color(0xFF7F0000)
        } else {
            val fraction = convertRange(it, 4.5, 13.5, 0.0, 1.0).toFloat()
            lerp(Color(0xFF7FAF00), Color(0xFF7F0000), fraction)
        }
    }
}

data class EmptyDailyAggregation(override val dateTime: ZonedDateTime): DailyAggregation {
    override fun colorCode(): Color = Color(0xFFA1A1A1)
}
