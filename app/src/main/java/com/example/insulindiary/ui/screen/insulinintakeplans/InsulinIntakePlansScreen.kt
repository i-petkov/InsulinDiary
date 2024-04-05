package com.example.insulindiary.ui.screen.insulinintakeplans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulindiary.data.InsulinIntakePlan
import com.example.insulindiary.data.Intake
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime

@Composable
fun InsulinIntakePlansScreen(
    onBackPressed: ()->Unit,
    onEditIntakePlanPressed: (Long)->Unit,
    onCreateNewIntakePlanPressed: ()->Unit,
    viewModel: InsulinIntakePlansViewModelInterface
) {
    val intakePlans = viewModel.intakePlans.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp)
    ) {

        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp), text = "TITLE", textAlign = TextAlign.Center)

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
        ) {
            items(intakePlans.value) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                ) {
                    Text(modifier = Modifier
                        .weight(1F)
                        .padding(12.dp), text = it.name, textAlign = TextAlign.Center)
                    Icon(modifier = Modifier
                        .weight(0.2F)
                        .padding(12.dp)
                        .clickable { onEditIntakePlanPressed(it.id) }, imageVector = Icons.Default.Edit, contentDescription = "Edit Plan")
                    Icon(modifier = Modifier
                        .weight(0.2F)
                        .padding(12.dp)
                        .clickable { viewModel.delete(it) }, imageVector = Icons.Default.Clear, contentDescription = "Remove Plan")
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Button(modifier = Modifier
                .weight(1F)
                .padding(12.dp), onClick = { onCreateNewIntakePlanPressed() }) {
                Text(text = "Create New")
            }

            Button(modifier = Modifier
                .weight(1F)
                .padding(12.dp), onClick = { onBackPressed() }) {
                Text(text = "Back")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsulinIntakePlansScreenPreview() {
    val intakeList = listOf(
        Intake(LocalTime.of(7, 0), 10.0, "Fast"),
        Intake(LocalTime.of(13, 0), 10.0, "Fast"),
        Intake(LocalTime.of(19, 0), 10.0, "Fast"),
        Intake(LocalTime.of(22, 0), 10.0, "Slow"),
    )

    val intakePlans = listOf(
        InsulinIntakePlan(0, "4xIntake", intakeList),
        InsulinIntakePlan(0, "4xIntake1", intakeList),
        InsulinIntakePlan(0, "4xIntake2", intakeList)
    )

    @Suppress("ktlint:standard:comment-wrapping")
    InsulinIntakePlansScreen({/* no-op */}, { _ ->/* no-op */}, {/* no-op */}, object : InsulinIntakePlansViewModelInterface {
        override val intakePlans: StateFlow<List<InsulinIntakePlan>>
            get() = MutableStateFlow(intakePlans)

        override fun delete(intakePlan: InsulinIntakePlan) {
            /* no-op */
        }
    })
}