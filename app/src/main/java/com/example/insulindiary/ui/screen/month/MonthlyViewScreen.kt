package com.example.insulindiary.ui.screen.month

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulindiary.data.DailyMeasurementAggregation
import com.example.insulindiary.data.Measurement
import com.example.insulindiary.data.dayOfMonth
import com.example.insulindiary.data.formatMonthAndYear
import com.example.insulindiary.ui.theme.Gray21A50
import com.example.insulindiary.ui.theme.InsulinDiaryTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

@Composable
fun MonthlyViewScreen(
    onDayPressed: (LocalDate) -> Unit,
    onSettingsPressed: () -> Unit,
    viewModel: MonthlyViewViewModelInterface
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        val monthAndYear = viewModel.monthAndYear.collectAsStateWithLifecycle(initialValue = LocalDate.now())
        val measurements = viewModel.dailyAggregations.collectAsStateWithLifecycle(initialValue = emptyList())

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(modifier = Modifier.padding(12.dp).clickable { onSettingsPressed() }, imageVector = Icons.Default.Settings, contentDescription = "Go to Application Settings")
            Text(
                text = monthAndYear.value.formatMonthAndYear(),
                modifier = Modifier.padding(12.dp),
                textDecoration = TextDecoration.Underline
            )
        }

        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
            items(measurements.value) {
                val isInRange =
                    monthAndYear.value.month == it.date.month &&
                        monthAndYear.value.year == it.date.year

                val mod =
                    if (isInRange) {
                        Modifier
                            .aspectRatio(1f)
                            .background(it.colorCode())
                            .clickable { onDayPressed(it.date) }
                    } else {
                        Modifier
                            .aspectRatio(1f)
                            .background(Gray21A50.compositeOver(it.colorCode()))
                    }

                Box(
                    modifier = mod,
                    contentAlignment = Alignment.TopEnd
                ) {
                    Text(text = it.dayOfMonth.toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthScreenPreview() {
    fun buildDummyDailyAggregation(
        date: LocalDate,
        time: LocalTime,
        measurement: Double
    ): DailyMeasurementAggregation {
        return DailyMeasurementAggregation(date, listOf(Measurement(date, time, measurement)))
    }

    val dateNow = LocalDate.now()
    val timeNow = LocalTime.now()
    val random = Random(0xDEAD_BEEF)
    val dummy =
        List(31) {
            buildDummyDailyAggregation(
                dateNow.plusDays(it.toLong()),
                timeNow,
                4.5 + random.nextDouble(8.0)
            )
        }

    InsulinDiaryTheme {
        MonthlyViewScreen(
            { /* no-op */ },
            { /* no-op */ },
            object : MonthlyViewViewModelInterface {
                override val dailyAggregations: StateFlow<List<DailyMeasurementAggregation>>
                    get() = MutableStateFlow(dummy)
                override val monthAndYear: StateFlow<LocalDate>
                    get() = MutableStateFlow(dateNow)
            }
        )
    }
}
