package com.example.insulindiary.ui.screen.insulinintake

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulindiary.data.InsulinIntakePlan
import com.example.insulindiary.data.Intake
import com.example.insulindiary.data.formatTime
import com.example.insulindiary.util.toLocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsulinIntakePlanBuilderScreen(
    onBackPressed: () -> Unit,
    viewModel: InsulinIntakePlanBuilderViewModelInterface
) {
    val intakeBuilderOpen = remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            text = "Create Insulin Intake",
            textAlign = TextAlign.Center
        )

        val intakes = viewModel.intakes.collectAsStateWithLifecycle(initialValue = emptyList())
        val baseIntakesNames = viewModel.baseIntakesNames.collectAsStateWithLifecycle(initialValue = emptyList())

        val nameState = remember { mutableStateOf("") }
        val isNameValid =
            nameState.value.trim().isNotBlank() &&
                !baseIntakesNames.value.contains(nameState.value)

        TextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            value = nameState.value,
            onValueChange = { nameState.value = it },
            isError = !isNameValid,
            label = {
                Text(text = "Name")
            }
        )

        Button(onClick = { intakeBuilderOpen.value = true }) {
            Text(text = "Add Intake")
        }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
        ) {
            items(intakes.value) {
                // intakes view
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                ) {
                    Text(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .weight(1F),
                        text = it.time.formatTime(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .weight(1F),
                        text = it.dosage.toString(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .weight(1F),
                        text = it.type,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Remove Intake",
                        modifier =
                            Modifier
                                .weight(0.3F)
                                .clickable { viewModel.removeIntake(it) }
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                modifier = Modifier.padding(12.dp),
                onClick = {
                    viewModel.storeBaseIntake(InsulinIntakePlan(0, nameState.value, intakes = intakes.value))
                    onBackPressed()
                },
                enabled = isNameValid
            ) {
                Text(text = "Store Intake")
            }

            Button(
                modifier = Modifier.padding(12.dp),
                onClick = {
                    onBackPressed()
                }
            ) {
                Text(text = "Cancel")
            }
        }
    }

    if (intakeBuilderOpen.value) {
        Dialog(onDismissRequest = { intakeBuilderOpen.value = false }) {
            Column(
                modifier =
                    Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val timePickerState = rememberTimePickerState()

                TimePicker(state = timePickerState, modifier = Modifier.padding(12.dp))

                val dosage = remember { mutableStateOf("") }
                val type = remember { mutableStateOf("") }

                val dosageIsValid =
                    runCatching { dosage.value.toDouble() }.getOrNull()
                        ?.let { it in 0.0..50.0 }
                        ?: false

                val typeIsValid = type.value.isNotBlank()

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                ) {
                    TextField(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(12.dp),
                        isError = !dosageIsValid,
                        label = { Text(text = "dosage") },
                        value = dosage.value,
                        onValueChange = { dosage.value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    TextField(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(12.dp),
                        isError = !typeIsValid,
                        label = { Text(text = "type") },
                        value = type.value,
                        onValueChange = { type.value = it }
                    )
                }

                Row {
                    Button(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(12.dp),
                        onClick = {
                            viewModel.addIntake(
                                Intake(
                                    timePickerState.toLocalTime(),
                                    dosage.value.toDouble(),
                                    type.value
                                )
                            )
                            intakeBuilderOpen.value = false
                        },
                        enabled = dosageIsValid && typeIsValid
                    ) {
                        Text(text = "OK")
                    }

                    Button(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(12.dp),
                        onClick = { intakeBuilderOpen.value = false }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsulinIntakePlanBuilderScreenPreview() {
    InsulinIntakePlanBuilderScreen(
        { /* no-op */ },
        object : InsulinIntakePlanBuilderViewModelInterface {
            override val intakes: StateFlow<List<Intake>>
                get() =
                    MutableStateFlow(
                        listOf(
                            Intake(LocalTime.now(), 20.0, "Tresiba"),
                            Intake(LocalTime.now(), 20.0, "Tresiba"),
                            Intake(LocalTime.now(), 20.0, "Tresiba")
                        )
                    )
            override val baseIntakesNames: StateFlow<List<String>>
                get() = MutableStateFlow(emptyList())

            override fun addIntake(intake: Intake) {
                // no-op
            }

            override fun removeIntake(intake: Intake) {
                // no-op
            }

            override fun storeBaseIntake(insulinIntakePlan: InsulinIntakePlan) {
                // no-op
            }
        }
    )
}
