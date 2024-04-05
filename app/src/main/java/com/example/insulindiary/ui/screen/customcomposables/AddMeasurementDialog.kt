package com.example.insulindiary.ui.screen.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.insulindiary.data.formatTime
import com.example.insulindiary.util.toLocalTime
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeasurementDialog(
    currentDate: LocalDate,
    onMeasurementNewMeasurement: (LocalDate, LocalTime, Double) -> Unit,
    onDismissRequest: () -> Unit
) {
    val timePickerOpen = remember { mutableStateOf(false) }
    val timeState: TimePickerState = rememberTimePickerState()
    var valueText by remember { mutableStateOf("") }
    val isValueValid = runCatching { valueText.toDouble() }.getOrNull()?.let { it in 0.0..50.0 } ?: false

    val backgroundColor = MaterialTheme.colorScheme.background

    Box(modifier = Modifier.fillMaxWidth()) {
        Dialog(
            onDismissRequest = { onDismissRequest() }
        ) {
            val time = timeState.toLocalTime()

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(backgroundColor),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                    text = "Set Measurement",
                    textAlign = TextAlign.End
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Box(
                            modifier =
                                Modifier
                                    .padding(12.dp)
                        ) {
                            TextField(
                                value = time.formatTime(),
                                label = {
                                    Text(text = "time")
                                },
                                singleLine = true,
                                readOnly = true,
                                onValueChange = { /* no-op */ },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                            Text(
                                modifier =
                                    Modifier.matchParentSize()
                                        .clickable { timePickerOpen.value = true },
                                text = ""
                            )
                        }
                    }
                    item {
                        TextField(
                            modifier =
                                Modifier
                                    .padding(12.dp),
                            value = valueText,
                            label = {
                                Text(text = "value")
                            },
                            isError = !isValueValid,
                            singleLine = true,
                            onValueChange = { valueText = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier =
                            Modifier
                                .width(IntrinsicSize.Min)
                                .fillMaxWidth(0.5F),
                        onClick = {
                            val v = valueText.toDouble()
                            onMeasurementNewMeasurement(currentDate, time, v)
                            onDismissRequest()
                        },
                        enabled = isValueValid
                    ) {
                        Text(text = "OK")
                    }

                    Button(
                        modifier =
                            Modifier
                                .width(IntrinsicSize.Min)
                                .fillMaxWidth(0.5F),
                        onClick = { onDismissRequest() }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }

        if (timePickerOpen.value) {
            Dialog(
                onDismissRequest = { timePickerOpen.value = false }
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .background(backgroundColor),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    TimePicker(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, bottom = 12.dp, start = 12.dp, end = 12.dp),
                        state = timeState,
                        layoutType = TimePickerDefaults.layoutType()
                    )

                    Button(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 24.dp, start = 12.dp, end = 12.dp),
                        onClick = { timePickerOpen.value = false }
                    ) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddMeasurementPickerPreview() {
    @Suppress("ktlint:standard:comment-wrapping")
    AddMeasurementDialog(LocalDate.now(), { date, time, value -> /* no-op */ }, { /* no-op */ })
}
