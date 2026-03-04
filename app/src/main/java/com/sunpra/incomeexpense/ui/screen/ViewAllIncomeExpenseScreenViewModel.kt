package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDataStore
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.ServiceProvider
import com.sunpra.incomeexpense.model.IncomeExpenseAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ViewAllIncomeExpenseScreenViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository: Repository = Repository(ServiceProvider.tipsService, AppDatabase.getInstance(application))
    private val dataStore: AppDataStore = AppDataStore(application)

    val incomeExpenses = dataStore.loggedInUserIdFlow().filterNotNull()
        .flatMapLatest { repository.getAllIncomeExpenses(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    private val _addOrUpdateIncomeExpense = MutableStateFlow<AddOrUpdateData?>(null)
    val addOrUpdateIncomeExpense = _addOrUpdateIncomeExpense.asStateFlow()

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

    fun dismissAddOrUpdateIncomeExpenseSheet() {
        _addOrUpdateIncomeExpense.update { null }
    }

}