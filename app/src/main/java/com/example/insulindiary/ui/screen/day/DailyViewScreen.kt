package com.example.insulindiary.ui.screen.day

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulindiary.data.Measurement
import com.example.insulindiary.data.formatDayMonthAndYear
import com.example.insulindiary.data.formatTime
import com.example.insulindiary.ui.screen.customcomposables.AddMeasurementDialog
import com.example.insulindiary.ui.theme.InsulinDiaryTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.ZonedDateTime

@Composable
fun DailyViewScreen(onBackPressed: ()-> Unit, viewModel: DailyViewViewModelInterface) {
    Column {

        val measurements = viewModel.measurements.collectAsStateWithLifecycle(initialValue = listOf())
        val date = viewModel.day.collectAsStateWithLifecycle(initialValue = ZonedDateTime.now())

        Text(
            text = date.value.formatDayMonthAndYear(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.End,
            textDecoration = TextDecoration.Underline
        )

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
            items(measurements.value) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = it.time.formatTime())
                    Text(text = it.value.toString())
                }
            }
        }

        val inputMeasurementOpen = remember { mutableStateOf(false) }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { inputMeasurementOpen.value = true }) {
                Text(text = "Add New Measurement")
            }

            Button(onClick = onBackPressed) {
                Text(text = "Back")
            }
        }

        if (inputMeasurementOpen.value) {
            AddMeasurementDialog(
                date.value,
                onMeasurementNewMeasurement = { time, value -> viewModel.insertMeasurement(time, value) },
                onDismissRequest = { inputMeasurementOpen.value = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyViewScreenPreview() {
    val now = ZonedDateTime.now()

    val measurements = listOf(
        Measurement(now, 6.5),
        Measurement(now, 6.5),
        Measurement(now, 6.5),
        Measurement(now, 6.5),
        Measurement(now, 6.5)
    )

    InsulinDiaryTheme {
        DailyViewScreen(onBackPressed = { /* no-op */ }, object : DailyViewViewModelInterface {
            override val measurements: StateFlow<List<Measurement>>
                get() = MutableStateFlow(measurements)
            override val day: StateFlow<ZonedDateTime>
                get() = MutableStateFlow(now)

            override fun insertDummyMeasurement() {
                /* no-op */
            }

            override fun insertMeasurement(time: ZonedDateTime, value: Double) {
                /* no-op */
            }
        })
    }
}