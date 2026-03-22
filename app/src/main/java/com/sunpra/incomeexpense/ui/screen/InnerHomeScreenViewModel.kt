package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDataStore
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.UserTable
import com.sunpra.incomeexpense.model.TimeFilterOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class InnerHomeScreenViewModel(application: Application): AndroidViewModel(application) {

    private val dataStore: AppDataStore = AppDataStore(context = application)
    private val repository: Repository = Repository(AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(InnerHomeScreenUIState())
    val uiState = _uiState.asStateFlow()

    val user: StateFlow<UserTable?> = dataStore.loggedInUserIdFlow()
        .filterNotNull()
        .map { id -> repository.getUserById(id) } // Object
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val incomeAndExpenses: StateFlow<List<IncomeExpenseTable>> = user.filterNotNull()
        .flatMapLatest { user -> repository.getAllIncomeExpense(user.id) } // Flow
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    fun onTimeFilterOptionChanged(timeFilterOption: TimeFilterOption){
        _uiState.update { state ->
            state.copy(timeFilterOption = timeFilterOption)
        }
    }

}

data class InnerHomeScreenUIState(
    val timeFilterOption: TimeFilterOption = TimeFilterOption.Daily
)