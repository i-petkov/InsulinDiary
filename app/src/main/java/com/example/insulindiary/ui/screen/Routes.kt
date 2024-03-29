package com.example.insulindiary.ui.screen

sealed class Routes(val route: String) {
    object MonthlyView: Routes("MonthlyView")
    object DailyView: Routes("DailyView")
}