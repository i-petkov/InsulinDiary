package com.example.insulindiary.ui.screen.insulinintakeplans

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulindiary.InsulinDiaryApplication
import com.example.insulindiary.data.InsulinIntakePlan
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface InsulinIntakePlansViewModelInterface {
    val intakePlans: StateFlow<List<InsulinIntakePlan>>

    fun delete(intakePlan: InsulinIntakePlan)
}

class InsulinIntakePlansViewModel(application: Application) : AndroidViewModel(application), InsulinIntakePlansViewModelInterface {
    private val insulinIntakeDao = (application as InsulinDiaryApplication).insulinIntakeDao

    override val intakePlans: StateFlow<List<InsulinIntakePlan>> =
        insulinIntakeDao.getAllInsulinIntakePlans()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    override fun delete(intakePlan: InsulinIntakePlan) {
        viewModelScope.launch {
            insulinIntakeDao.delete(intakePlan)
        }
    }
}
