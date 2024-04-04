package com.example.insulindiary.ui.screen.day

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulindiary.InsulinDiaryApplication
import com.example.insulindiary.data.Measurement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

interface DailyViewViewModelInterface {
    val measurements: StateFlow<List<Measurement>>
    val day: StateFlow<LocalDate>

    fun insertDummyMeasurement()

    fun insertMeasurement(
        date: LocalDate,
        time: LocalTime,
        value: Double
    )
}

class DailyViewViewModel(application: Application) : AndroidViewModel(application), DailyViewViewModelInterface {
    private val measurementDao = (application as InsulinDiaryApplication).measurementDao

    private val _day = MutableStateFlow((application as InsulinDiaryApplication).selectedDay)
    override val day: StateFlow<LocalDate> = _day

    override val measurements: StateFlow<List<Measurement>> =
        measurementDao.getAllItemsAt(day.value.toEpochDay())
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    override fun insertDummyMeasurement() {
        insertMeasurement(LocalDate.now(), LocalTime.now(), 1.1)
    }

    override fun insertMeasurement(
        date: LocalDate,
        time: LocalTime,
        value: Double
    ) {
        viewModelScope.launch {
            measurementDao.insert(Measurement(date, time, value))
        }
    }
}
