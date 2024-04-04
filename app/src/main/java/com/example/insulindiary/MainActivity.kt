package com.example.insulindiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.insulindiary.data.Measurement
import com.example.insulindiary.ui.screen.Routes
import com.example.insulindiary.ui.screen.day.DailyViewScreen
import com.example.insulindiary.ui.screen.day.DailyViewViewModel
import com.example.insulindiary.ui.screen.month.MonthlyViewScreen
import com.example.insulindiary.ui.screen.month.MonthlyViewViewModel
import com.example.insulindiary.ui.theme.InsulinDiaryTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        seedMeasurementForTesting()
        val application = application as InsulinDiaryApplication

        setContent {
            InsulinDiaryTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.MonthlyView.route) {
                    composable(Routes.MonthlyView.route) {
                        MonthlyViewScreen(onDayClicked = { day ->
                            application.selectDay(day)
                            navController.navigate(Routes.DailyView.route)
                        }, viewModel<MonthlyViewViewModel>())
                    }
                    composable(Routes.DailyView.route) {
                        DailyViewScreen(onBackPressed = {
                            navController.popBackStack()
                        }, viewModel<DailyViewViewModel>())
                    }
                }
            }
        }
    }

    private fun seedMeasurementForTesting() {
        val dao = (application as InsulinDiaryApplication).measurementDao
        GlobalScope.launch {
            dao.getAllItems().collect {
                if (it.isEmpty()) {
                    val date = LocalDate.now()
                    val time = LocalTime.now()

                    dao.insert(Measurement(date, time, 6.5))
                    dao.insert(Measurement(date.plusDays(1), time, 6.5))
                    dao.insert(Measurement(date.plusDays(2), time, 6.5))
                }
            }
        }
    }
}
