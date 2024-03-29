package com.example.insulindiary

import android.app.Application
import com.example.insulindiary.data.MeasurementDao
import com.example.insulindiary.data.MeasurementsDatabase

class InsulinDiaryApplication: Application() {

    val measurementDao: MeasurementDao by lazy {
        MeasurementsDatabase.getDatabase(this).measurementDao()
    }
}