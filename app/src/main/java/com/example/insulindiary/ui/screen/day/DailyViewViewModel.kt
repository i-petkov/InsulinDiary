package com.example.insulindiary.ui.screen.day

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulindiary.InsulinDiaryApplication
import com.example.insulindiary.data.Measurement
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

interface DailyViewViewModelInterface {
    val measurements: StateFlow<List<Measurement>>
    fun insertDummyMeasurement()
}

class DailyViewViewModel(application: Application) : AndroidViewModel(application), DailyViewViewModelInterface {
    private val measurementDao = (application as InsulinDiaryApplication).measurementDao

    override val measurements: StateFlow<List<Measurement>> = measurementDao.getAllItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    override fun insertDummyMeasurement() {
        viewModelScope.launch {
            measurementDao.insert(Measurement(ZonedDateTime.now(), 1.1))
        }
    }
}