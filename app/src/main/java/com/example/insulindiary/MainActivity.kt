package com.example.insulindiary

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.insulindiary.data.Measurement
import com.example.insulindiary.data.formatTime
import com.example.insulindiary.ui.theme.InsulinDiaryTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class MainActivity : ComponentActivity() {

    val viewModel: DayViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsulinDiaryTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting(viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(viewModel: DayViewModel) {

    Column {

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


}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    InsulinDiaryTheme {
//        Greeting("Android")
//    }
//}

class DayViewModel(application: Application) : AndroidViewModel(application) {
    private val measurementDao = (application as InsulinDiaryApplication).measurementDao

    val measurements: StateFlow<List<Measurement>> = measurementDao.getAllItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertDummyMeasurement() {
        viewModelScope.launch {
            measurementDao.insert(Measurement(ZonedDateTime.now(), 1.1))
        }
    }
}
