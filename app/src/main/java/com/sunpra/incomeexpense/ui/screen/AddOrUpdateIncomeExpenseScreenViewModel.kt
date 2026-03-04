package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDataStore
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.ExpenseType
import com.sunpra.incomeexpense.data.IncomeExpense
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import com.sunpra.incomeexpense.data.IncomeType
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.ServiceProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class AddOrUpdateIncomeExpenseScreenViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repository: Repository =
        Repository(ServiceProvider.tipsService, AppDatabase.getInstance(application))
    private val dataStore: AppDataStore = AppDataStore(application)


    private val user = dataStore.loggedInUserIdFlow().filterNotNull()
        .map { repository.getUserById(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _uiState = MutableStateFlow(AddOrUpdateIncomeExpenseScreenUIState())
    val uiState = _uiState.asStateFlow()

    private val _dismissSheet = MutableSharedFlow<Unit>()
    val dismissSheet = _dismissSheet.asSharedFlow()

    fun handleAddOrUpdateData(addOrUpdateData: AddOrUpdateData) {
        _uiState.update {
            when (addOrUpdateData.addOrUpdate) {
                AddOrUpdate.Add -> AddOrUpdateIncomeExpenseScreenUIState(
                    addOrUpdate = addOrUpdateData.addOrUpdate
                )

                AddOrUpdate.Update -> {
                    val incomeExpense = checkNotNull(addOrUpdateData.incomeExpense)

                    AddOrUpdateIncomeExpenseScreenUIState(
                        id = incomeExpense.id,
                        addOrUpdate = addOrUpdateData.addOrUpdate,
                        incomeExpense = incomeExpense.incomeExpense,
                        name = incomeExpense.name,
                        amount = incomeExpense.amount.toString(),
                        incomeType = incomeExpense.incomeType,
                        expenseType = incomeExpense.expenseType
                    )
                }
            }
        }
    }

    fun onIncomeExpenseChosen(incomeExpense: IncomeExpense) {
        _uiState.update { state ->
            state.copy(incomeExpense = incomeExpense, formErrors = mutableMapOf())
        }
    }

    fun onAmountChanged(value: String) {
        _uiState.update { state ->
            state.copy(amount = value)
        }
    }

    fun onNameChanged(value: String) {
        _uiState.update { state ->
            state.copy(name = value)
        }
    }

    fun toggleIncomeTypeDropdown() {
        _uiState.update { state ->
            state.copy(showIncomeTypeDropdown = state.showIncomeTypeDropdown.not())
        }
    }

    fun onIncomeTypeSelected(incomeType: IncomeType) {
        _uiState.update { state ->
            state.copy(incomeType = incomeType, expenseType = null, showIncomeTypeDropdown = false)
        }
    }

    fun toggleExpenseTypeDropdown() {
        _uiState.update { state ->
            state.copy(showExpenseTypeDropdown = state.showExpenseTypeDropdown.not())
        }
    }

    fun onExpenseTypeSelected(expenseType: ExpenseType) {
        _uiState.update { state ->
            state.copy(
                expenseType = expenseType,
                incomeType = null,
                showExpenseTypeDropdown = false
            )
        }
    }

    private fun validate(): Boolean {
        val errors = mutableMapOf<String, String>()

        val uiState = uiState.value

        if (uiState.name.trim().isEmpty()) {
            errors["Name"] = "Name is required."
        }

        if (uiState.amount.trim().isEmpty()) {
            errors["Amount"] = "Amount is required."
        } else if (uiState.amount.toDoubleOrNull() == null) {
            errors["Amount"] = "Amount must be number."
        }

        when (uiState.incomeExpense) {
            IncomeExpense.Income -> {
                if (uiState.incomeType == null) {
                    errors["IncomeType"] = "Income type not selected."
                }
            }

            IncomeExpense.Expense -> {
                if (uiState.expenseType == null) {
                    errors["ExpenseType"] = "Expense type not selected."
                }
            }
        }

        _uiState.update { state ->
            state.copy(formErrors = errors)
        }

        return errors.isEmpty()
    }

    fun onSubmitButtonClicked() {
        if (validate()) {
            val user = user.value ?: return
            viewModelScope.launch {
                val uiState = _uiState.value
                val id = uiState.id
                val incomeExpense = uiState.incomeExpense
                val name = uiState.name
                val amount = uiState.amount.toDouble()
                val incomeType = if (incomeExpense == IncomeExpense.Income) uiState.incomeType
                else null
                val expenseType = if (incomeExpense == IncomeExpense.Expense) uiState.expenseType
                else null
                val userId = user.id
                val incomeExpenseTable = IncomeExpenseTable(
                    id = id,
                    incomeExpense = incomeExpense,
                    name = name,
                    amount = amount,
                    incomeType = incomeType,
                    expenseType = expenseType,
                    date = Calendar.getInstance().timeInMillis,
                    userId = userId
                )

                when (checkNotNull(uiState.addOrUpdate)) {
                    AddOrUpdate.Add -> repository.insertIncomeExpense(incomeExpenseTable)
                    AddOrUpdate.Update -> repository.updateIncomeExpense(incomeExpenseTable)
                }

                _dismissSheet.emit(Unit)
            }
        }
    }
}

data class AddOrUpdateIncomeExpenseScreenUIState(
    val id: String = UUID.randomUUID().toString(),
    val addOrUpdate: AddOrUpdate? = null,
    val incomeExpense: IncomeExpense = IncomeExpense.Income,
    val name: String = "",
    val amount: String = "",
    val incomeType: IncomeType? = null,
    val showIncomeTypeDropdown: Boolean = false,
    val expenseType: ExpenseType? = null,
    val showExpenseTypeDropdown: Boolean = false,
    val formErrors: MutableMap<String, String> = mutableMapOf()
)