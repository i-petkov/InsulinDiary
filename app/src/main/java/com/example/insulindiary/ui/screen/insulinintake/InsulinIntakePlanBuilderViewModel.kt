package com.example.insulindiary.ui.screen.insulinintake

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulindiary.InsulinDiaryApplication
import com.example.insulindiary.data.InsulinIntakePlan
import com.example.insulindiary.data.Intake
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface InsulinIntakePlanBuilderViewModelInterface {
    val intakes: StateFlow<List<Intake>>
    val baseIntakesNames: StateFlow<List<String>>

    fun addIntake(intake: Intake)

    fun removeIntake(intake: Intake)

    fun storeBaseIntake(insulinIntakePlan: InsulinIntakePlan)
}

class InsulinIntakePlanBuilderViewModel(application: Application) : AndroidViewModel(application), InsulinIntakePlanBuilderViewModelInterface {
    private val insulinIntakeDao = (application as InsulinDiaryApplication).insulinIntakeDao

    private val _intakes = MutableStateFlow(emptyList<Intake>())

    override val intakes: StateFlow<List<Intake>> = _intakes
    override val baseIntakesNames: StateFlow<List<String>> = insulinIntakeDao.getAllInsulinIntakePlans()
        .map { plans -> plans.map { it.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    override fun addIntake(intake: Intake) {
        _intakes.value = _intakes.value + intake
    }

    override fun removeIntake(intake: Intake) {
        _intakes.value = _intakes.value - intake
    }

    override fun storeBaseIntake(insulinIntakePlan: InsulinIntakePlan) {
        viewModelScope.launch {
            insulinIntakeDao.insert(insulinIntakePlan)
        }
    }
}