package com.example.insulindiary.ui.screen.month

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
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
import java.time.ZonedDateTime
import kotlin.random.Random

@Composable
fun MonthlyViewScreen(onDayClicked: (ZonedDateTime)-> Unit, viewModel: MonthlyViewViewModelInterface) {
    Column(
        horizontalAlignment = Alignment.End
    ) {

        val monthAndYear = viewModel.monthAndYear.collectAsStateWithLifecycle(initialValue = ZonedDateTime.now())
        val measurements = viewModel.dailyAggregations.collectAsStateWithLifecycle(initialValue = emptyList())

        Text(
            text = monthAndYear.value.formatMonthAndYear(),
            modifier = Modifier.padding(12.dp)
        )

        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
            items(measurements.value) {
                val isInRange = monthAndYear.value.month == it.dateTime.month &&
                        monthAndYear.value.year == it.dateTime.year

                val mod = if (isInRange) {
                    Modifier
                        .aspectRatio(1f)
                        .background(it.colorCode())
                        .clickable { onDayClicked(it.dateTime) }
                } else {
                    Modifier
                        .aspectRatio(1f)
                        .background(Gray21A50.compositeOver(it.colorCode())) // TODO custom colors
                }

                Box(
                    modifier = mod,
                    contentAlignment = Alignment.TopEnd,
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
    fun buildDummyDailyAggregation(dateTime: ZonedDateTime, measurement: Double): DailyMeasurementAggregation {
        return DailyMeasurementAggregation(dateTime, listOf(Measurement(dateTime, measurement)))
    }

    val now = ZonedDateTime.now()
    val random = Random(0xDEAD_BEEF)
    val dummy = List(31) {
        buildDummyDailyAggregation(
            now.plusDays(it.toLong()),
            4.5 + random.nextDouble(8.0)
        )
    }

    InsulinDiaryTheme {
        MonthlyViewScreen({ /* no-op */ }, object : MonthlyViewViewModelInterface {
            override val dailyAggregations: StateFlow<List<DailyMeasurementAggregation>>
                get() = MutableStateFlow(dummy)
            override val monthAndYear: StateFlow<ZonedDateTime>
                get() = MutableStateFlow(now)
        })
    }
}