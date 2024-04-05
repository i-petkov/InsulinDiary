package com.example.insulindiary.ui.screen

sealed class Routes(val route: String) {
    object MonthlyView : Routes("MonthlyView")

    object DailyView : Routes("DailyView")

    object Settings : Routes("Settings")

    object InsulinIntakePlans : Routes("InsulinIntakePlans")

    object InsulinIntakePlanBuilder : Routes("InsulinIntakePlanBuilder")
}
