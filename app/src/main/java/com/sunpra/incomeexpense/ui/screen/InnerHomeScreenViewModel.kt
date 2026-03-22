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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

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

    private val incomeAndExpenses: StateFlow<List<IncomeExpenseTable>> = user.filterNotNull()
        .flatMapLatest { user -> repository.getAllIncomeExpense(user.id) } // Flow
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    val filteredList = combine(
        _uiState.map { it.timeFilterOption },
        incomeAndExpenses,
        ::Pair
    ).map { (timeFilterOption, incomeAndExpenses) ->
        when(timeFilterOption){
            TimeFilterOption.Daily -> {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time
                incomeAndExpenses.filter {
                    val createDate = Calendar.getInstance().apply { timeInMillis = it.dateCreated }.time
                    createDate.after(calendar)
                }
            }
            TimeFilterOption.Weekly -> {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    add(Calendar.WEEK_OF_MONTH, -1)
                }.time
                incomeAndExpenses.filter {
                    val time = Calendar.getInstance().apply { timeInMillis = it.dateCreated }.time
                    time.after(calendar)
                }
            }
            TimeFilterOption.Monthly -> {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    add(Calendar.MONTH, -1)
                }.time
                incomeAndExpenses.filter {
                    val time = Calendar.getInstance().apply { timeInMillis = it.dateCreated }.time
                    time.after(calendar)
                }
            }
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    fun onTimeFilterOptionChanged(timeFilterOption: TimeFilterOption){
        _uiState.update { state ->
            state.copy(timeFilterOption = timeFilterOption)
        }
    }

    fun onDeleteClick(incomeExpenseTable: IncomeExpenseTable){
        viewModelScope.launch {
            repository.deleteIncomeExpense(incomeExpenseTable)
        }
    }

}

data class InnerHomeScreenUIState(
    val timeFilterOption: TimeFilterOption = TimeFilterOption.Daily
)