package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDataStore
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.IncomeExpense
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.ServiceProvider
import com.sunpra.incomeexpense.data.calendarDate
import com.sunpra.incomeexpense.model.IncomeExpenseAction
import com.sunpra.incomeexpense.model.TimeFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository(ServiceProvider.tipsService, AppDatabase.getInstance(application))
    private val dataStore: AppDataStore = AppDataStore(application)

    private val _uiState = MutableStateFlow(DashboardScreenUIState())
    val uiState = _uiState.asStateFlow()

    val user = dataStore.loggedInUserIdFlow().filterNotNull()
        .map { repository.getUserById(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val dailySavingGoal = dataStore.dailySavingGoalFlow().filterNotNull()
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    private val incomeExpenses = dataStore.loggedInUserIdFlow().filterNotNull()
        .flatMapLatest { repository.getAllIncomeExpenses(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    private val todayIncomeExpenses = incomeExpenses.map {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        it.filter { item -> item.calendarDate.after(calendar) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    val todayIncome = todayIncomeExpenses
        .map { it.filter { item -> item.incomeExpense == IncomeExpense.Income } }
        .map { it.sumOf { item -> item.amount } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val todayExpense = todayIncomeExpenses
        .map { it.filter { item -> item.incomeExpense == IncomeExpense.Expense } }
        .map { it.sumOf { item -> item.amount } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val dailySavingGoalAchieved = combine(
        dailySavingGoal,
        todayIncome,
        ::Pair
    ).map { (goalAmount, todayIncome) ->
        if (goalAmount > 0.0 && todayIncome > 0.0) {
            todayIncome.div(goalAmount).coerceAtMost(1.0)
        } else {
            0.0
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val filteredIncomeExpense = combine(
        uiState.map { it.timeFilter },
        uiState.map { it.incomeExpenseFilter },
        incomeExpenses
    ) { timeFilter, incomeExpenseFilter, incomeExpenses ->
        CombinedData(
            timeFilter,
            incomeExpenseFilter,
            incomeExpenses
        )
    }.map { combinedData ->
        when (combinedData.timeFilter) {
            TimeFilter.Daily -> {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time
                combinedData.incomeExpenses.filter { it.calendarDate.after(calendar) }
            }

            TimeFilter.Weekly -> {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    add(Calendar.WEEK_OF_MONTH, -1)
                }.time
                combinedData.incomeExpenses.filter { it.calendarDate.after(calendar) }
            }

            TimeFilter.Monthly -> {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    add(Calendar.MONTH, -1)
                }.time
                combinedData.incomeExpenses.filter { it.calendarDate.after(calendar) }
            }
        }.filter { combinedData.incomeExpenseFilter == null || combinedData.incomeExpenseFilter == it.incomeExpense }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    private val _addOrUpdateIncomeExpense = MutableStateFlow<AddOrUpdateData?>(null)
    val addOrUpdateIncomeExpense = _addOrUpdateIncomeExpense.asStateFlow()

    fun addNewIncomeOrExpense() {
        _addOrUpdateIncomeExpense.update {
            AddOrUpdateData(
                addOrUpdate = AddOrUpdate.Add,
                incomeExpense = null
            )
        }
    }

    fun dismissAddOrUpdateIncomeExpenseSheet() {
        _addOrUpdateIncomeExpense.update { null }
    }

    fun onTimeFilterChanged(timeFilter: TimeFilter) {
        _uiState.update { state ->
            state.copy(timeFilter = timeFilter)
        }
    }

    fun onIncomeExpenseFilterChanged(incomeExpenseFilter: IncomeExpense?) {
        _uiState.update { state ->
            state.copy(
                incomeExpenseFilter = if (incomeExpenseFilter == state.incomeExpenseFilter) null else incomeExpenseFilter,
                showIncomeExpenseFilterOptions = false
            )
        }
    }

    fun toggleShowIncomeExpenseFilterOptions() {
        _uiState.update { state ->
            state.copy(showIncomeExpenseFilterOptions = state.showIncomeExpenseFilterOptions.not())
        }
    }

    fun onIncomeExpenseActionSelected(
        incomeExpenseAction: IncomeExpenseAction,
        incomeExpenseTable: IncomeExpenseTable
    ) {
        when (incomeExpenseAction) {
            IncomeExpenseAction.Update -> {
                _addOrUpdateIncomeExpense.update {
                    AddOrUpdateData(
                        addOrUpdate = AddOrUpdate.Update,
                        incomeExpense = incomeExpenseTable
                    )
                }
            }

            IncomeExpenseAction.Delete -> {
                viewModelScope.launch {
                    repository.deleteIncomeExpense(incomeExpenseTable)
                }
            }
        }
    }

    fun toggleShowUpdateDailySavingDialog() {
        _uiState.update { state ->
            state.copy(showUpdateDailySavingDialog = state.showUpdateDailySavingDialog.not())
        }
    }

    fun onDailySavingAmountUpdated(amount: Double) {
        viewModelScope.launch {
            dataStore.saveDailySavingGoal(amount)
        }
    }
}

data class DashboardScreenUIState(
    val showUpdateDailySavingDialog: Boolean = false,
    val timeFilter: TimeFilter = TimeFilter.Daily,
    val incomeExpenseFilter: IncomeExpense? = null,
    val showIncomeExpenseFilterOptions: Boolean = false
)

private data class CombinedData(
    val timeFilter: TimeFilter,
    val incomeExpenseFilter: IncomeExpense?,
    val incomeExpenses: List<IncomeExpenseTable>
)