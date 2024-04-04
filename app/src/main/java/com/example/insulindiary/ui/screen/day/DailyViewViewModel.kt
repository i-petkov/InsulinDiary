package com.example.insulindiary.ui.screen.day

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulindiary.InsulinDiaryApplication
import com.example.insulindiary.data.Measurement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

interface DailyViewViewModelInterface {
    val measurements: StateFlow<List<Measurement>>
    val day: StateFlow<ZonedDateTime>

    fun insertDummyMeasurement()

    fun insertMeasurement(
        time: ZonedDateTime,
        value: Double
    )
}

class DailyViewViewModel(application: Application) : AndroidViewModel(application), DailyViewViewModelInterface {
    private val measurementDao = (application as InsulinDiaryApplication).measurementDao

    private val _day = MutableStateFlow((application as InsulinDiaryApplication).selectedDay)
    override val day: StateFlow<ZonedDateTime> = _day

    override val measurements: StateFlow<List<Measurement>> =
        aggregateMeasurements(day.value)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private fun aggregateMeasurements(date: ZonedDateTime): Flow<List<Measurement>> {
        val start = date.truncatedTo(ChronoUnit.DAYS)
        val end = start.plusDays(1)

        return measurementDao.getAllItemsBetween(start.toInstant().toEpochMilli(), end.toInstant().toEpochMilli())
    }

    override fun insertDummyMeasurement() {
        insertMeasurement(ZonedDateTime.now(), 1.1)
    }

    override fun insertMeasurement(
        time: ZonedDateTime,
        value: Double
    ) {
        viewModelScope.launch {
            measurementDao.insert(Measurement(time, value))
        }
    }
}
