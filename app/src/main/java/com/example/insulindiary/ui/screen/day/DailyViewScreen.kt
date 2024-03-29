package com.example.insulindiary.ui.screen.day

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulindiary.data.Measurement
import com.example.insulindiary.data.formatTime
import com.example.insulindiary.ui.theme.InsulinDiaryTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.ZonedDateTime

@Composable
fun DailyViewScreen(onBackPressed: ()-> Unit, viewModel: DailyViewViewModelInterface) {
    Text(
        text = "Hello MEASUREMENTS!",
        modifier = Modifier.fillMaxWidth()
    )

    val measurements = viewModel.measurements.collectAsStateWithLifecycle(initialValue = listOf())

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(measurements.value) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = it.time.formatTime())
                Text(text = it.value.toString())
            }
        }
    }

    Button(onClick = { viewModel.insertDummyMeasurement() }) {
        Text(text = "Add More")
    }
}

@Preview(showBackground = true)
@Composable
fun DailyViewScreenPreview() {
    val measurements = listOf(
        Measurement(ZonedDateTime.now(), 6.5),
        Measurement(ZonedDateTime.now(), 6.5),
        Measurement(ZonedDateTime.now(), 6.5),
        Measurement(ZonedDateTime.now(), 6.5),
        Measurement(ZonedDateTime.now(), 6.5)
    )

    InsulinDiaryTheme {
        DailyViewScreen(onBackPressed = { /* no-op */ }, object : DailyViewViewModelInterface {
            override val measurements: StateFlow<List<Measurement>>
                get() = MutableStateFlow(measurements)

            override fun insertDummyMeasurement() {
                /* no-op */
            }
        })
    }
}