package com.example.insulindiary.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toLocalTime() = LocalTime.of(hour, minute)
