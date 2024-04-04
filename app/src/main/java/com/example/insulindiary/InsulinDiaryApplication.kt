package com.example.insulindiary

import android.app.Application
import com.example.insulindiary.data.MeasurementDao
import com.example.insulindiary.data.MeasurementsDatabase
import java.time.LocalDate

class InsulinDiaryApplication : Application() {
    val measurementDao: MeasurementDao by lazy {
        MeasurementsDatabase.getDatabase(this).measurementDao()
    }

    var selectedDay = LocalDate.now()
        private set

    fun selectDay(day: LocalDate) {
        selectedDay = day
    }
}
