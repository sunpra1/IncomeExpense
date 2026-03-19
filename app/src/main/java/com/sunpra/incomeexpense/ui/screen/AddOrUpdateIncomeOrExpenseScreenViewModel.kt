package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDataStore
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.ExpenseType
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import com.sunpra.incomeexpense.data.IncomeOrExpense
import com.sunpra.incomeexpense.data.IncomeType
import com.sunpra.incomeexpense.data.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class AddOrUpdateIncomeOrExpenseScreenViewModel(application: Application) :
    AndroidViewModel(application) {
    private val dataStore: AppDataStore = AppDataStore(context = application)
    private val repository: Repository = Repository(AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(AddOrUpdateIncomeOrExpenseScreenUIState())
    val uiState = _uiState.asStateFlow()

    private val _navigateToHomeScreen = MutableSharedFlow<Unit>()
    val navigateToHomeScreen = _navigateToHomeScreen.asSharedFlow()

    fun onIncomeExpenseSelected(incomeOrExpense: IncomeOrExpense) {
        _uiState.update { oldState ->
            oldState.copy(
                incomeOrExpense = incomeOrExpense,
                incomeType = null,
                expenseType = null
            )
        }
    }

    fun onNameChanged(value: String) {
        _uiState.update { oldState ->
            oldState.copy(name = value)
        }
    }

    fun onAmountChanged(value: String) {
        _uiState.update { oldState ->
            oldState.copy(amount = value)
        }
    }

    fun onIncomeTypeSelected(incomeType: IncomeType) {
        _uiState.update { oldState ->
            oldState.copy(
                incomeType = incomeType,
                expenseType = null,
                showIncomeTypeDropdown = false
            )
        }
    }

    fun onExpenseTypeSelected(expenseType: ExpenseType) {
        _uiState.update { oldState ->
            oldState.copy(
                incomeType = null,
                expenseType = expenseType,
                showExpenseTypeDropdown = false
            )
        }
    }

    fun onNoteChanged(value: String) {
        _uiState.update { oldState ->
            oldState.copy(note = value)
        }
    }

    fun toggleIncomeTypeDropDown() {
        _uiState.update { oldState ->
            oldState.copy(showIncomeTypeDropdown = !oldState.showIncomeTypeDropdown)
        }
    }

    fun toggleExpenseTypeDropDown() {
        _uiState.update { oldState ->
            oldState.copy(showExpenseTypeDropdown = !oldState.showExpenseTypeDropdown)
        }
    }

    fun onFormSubmitted() {
        // TODO validate
        val uiState = _uiState.value

        val id = UUID.randomUUID().toString()
        val incomeOrExpense = uiState.incomeOrExpense
        val name = uiState.name
        val amount = uiState.amount.toDouble()
        val incomeType = uiState.incomeType
        val expenseType = uiState.expenseType
        val note = uiState.note.takeIf { it.isNotEmpty() }
        val dateCreated = Calendar.getInstance().timeInMillis

        viewModelScope.launch {
            val userId = dataStore.loggedInUserIdFlow().first() ?: return@launch

            val incomeExpenseTable = IncomeExpenseTable(
                id, incomeOrExpense, name, amount, incomeType,
                expenseType, note, dateCreated, userId
            )

            repository.addIncomeOrExpenseInTable(incomeExpenseTable)
            _uiState.update {
                AddOrUpdateIncomeOrExpenseScreenUIState()
            }
            _navigateToHomeScreen.emit(Unit)
        }
    }
}

data class AddOrUpdateIncomeOrExpenseScreenUIState(
    val incomeOrExpense: IncomeOrExpense = IncomeOrExpense.Income,
    val name: String = "",
    val amount: String = "",
    val incomeType: IncomeType? = null,
    val showIncomeTypeDropdown: Boolean = false,
    val expenseType: ExpenseType? = null,
    val showExpenseTypeDropdown: Boolean = false,
    val note: String = "",
    val errors: Map<String, String> = mapOf()
)