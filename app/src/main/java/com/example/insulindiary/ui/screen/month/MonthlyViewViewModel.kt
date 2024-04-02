package com.example.insulindiary.ui.screen.month

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulindiary.InsulinDiaryApplication
import com.example.insulindiary.data.DailyAggregation
import com.example.insulindiary.data.DailyMeasurementAggregation
import com.example.insulindiary.data.EmptyDailyAggregation
import com.example.insulindiary.data.toUtc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

interface MonthlyViewViewModelInterface {
    val monthAndYear: StateFlow<ZonedDateTime>
    val dailyAggregations: StateFlow<List<DailyAggregation>>
}
// TODO How do you deal with dates??
//  same instant of time can happen at a different day for different timezones
//  storing dates in user time zone makes them not transportable as well as erases information when a user changes its local time zone
//  need to investigate
class MonthlyViewViewModel(application: Application): AndroidViewModel(application), MonthlyViewViewModelInterface {
    private val measurementDao = (application as InsulinDiaryApplication).measurementDao
    private val _monthAndYear = MutableStateFlow(ZonedDateTime.now().toUtc().truncatedTo(ChronoUnit.DAYS))

    override val monthAndYear: StateFlow<ZonedDateTime> = _monthAndYear
    override val dailyAggregations: StateFlow<List<DailyAggregation>> = aggregate(_monthAndYear.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private fun aggregate(ref: ZonedDateTime): Flow<List<DailyAggregation>> {
        val refUtc = ref.toUtc()

        val start = refUtc.withDayOfMonth(1)
        val end = refUtc.plusMonths(1).withDayOfMonth(1)

        val queryBack = start.dayOfWeek.value - 1
        val queryForward = (7 - (end.dayOfWeek.value - 1)) % 7

        val exStart = start.minusDays(queryBack.toLong())
        val exEnd = end.plusDays(queryForward.toLong())

        val totalDays = ChronoUnit.DAYS.between(exStart, exEnd)

        return measurementDao.getAllItemsBetween(
            exStart.toInstant().toEpochMilli(),
            exEnd.toInstant().toEpochMilli()
        ).map {
            val groupedByDay = it.groupBy { measurement ->
                measurement.time.truncatedTo(ChronoUnit.DAYS)
            }

            List(totalDays.toInt()) { index ->
                val day = exStart.plusDays(index.toLong()).truncatedTo(ChronoUnit.DAYS)
                groupedByDay[day]
                    ?.let { measurements -> DailyMeasurementAggregation(day, measurements) }
                    ?: EmptyDailyAggregation(day)
            }
        }
    }
}