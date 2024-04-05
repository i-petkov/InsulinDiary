package com.example.insulindiary.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.insulindiary.ui.screen.Routes

@Composable
fun SettingsScreen(
    navigateTo: (Routes) -> Unit,
    onBackPressed: () -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(12.dp)
    ) {
        Button(onClick = { navigateTo(Routes.InsulinIntakePlans) }) {
            Text(text = "Insulin Intake Plans")
        }

        Button(onClick = { onBackPressed() }) {
            Text(text = "Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen({ /* no-op */ }, { /* no-op */ })
}
