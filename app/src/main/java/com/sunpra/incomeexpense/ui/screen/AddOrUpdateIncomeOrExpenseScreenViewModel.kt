package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sunpra.incomeexpense.data.ExpenseType
import com.sunpra.incomeexpense.data.IncomeOrExpense
import com.sunpra.incomeexpense.data.IncomeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddOrUpdateIncomeOrExpenseScreenViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(AddOrUpdateIncomeOrExpenseScreenUIState())
    val uiState = _uiState.asStateFlow()


    fun onIncomeExpenseSelected(incomeOrExpense: IncomeOrExpense){
        _uiState.update { oldState ->
            oldState.copy(
                incomeOrExpense = incomeOrExpense,
                incomeType = null,
                expenseType = null
            )
        }
    }

    fun onNameChanged(value: String){
        _uiState.update { oldState ->
            oldState.copy(name = value)
        }
    }

    fun onAmountChanged(value: String){
        _uiState.update { oldState ->
            oldState.copy(amount = value)
        }
    }

    fun onIncomeTypeSelected(incomeType: IncomeType){
        _uiState.update { oldState ->
            oldState.copy(incomeType = incomeType, expenseType = null)
        }
    }

    fun onExpenseTypeSelected(expenseType: ExpenseType){
        _uiState.update { oldState ->
            oldState.copy(incomeType = null, expenseType = expenseType)
        }
    }

    fun onNoteChanged(value: String){
        _uiState.update { oldState ->
            oldState.copy(note = value)
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