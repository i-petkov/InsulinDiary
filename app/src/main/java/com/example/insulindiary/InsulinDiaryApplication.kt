package com.example.insulindiary

import android.app.Application
import com.example.insulindiary.data.MeasurementDao
import com.example.insulindiary.data.MeasurementsDatabase
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class InsulinDiaryApplication: Application() {

    val measurementDao: MeasurementDao by lazy {
        MeasurementsDatabase.getDatabase(this).measurementDao()
    }

    var selectedDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        private set

    fun selectDay(day: ZonedDateTime) {
        selectedDay = day.truncatedTo(ChronoUnit.DAYS)
    }
}