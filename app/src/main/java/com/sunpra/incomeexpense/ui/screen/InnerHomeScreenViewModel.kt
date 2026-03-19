package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDataStore
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.UserTable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class InnerHomeScreenViewModel(application: Application): AndroidViewModel(application) {

    private val dataStore: AppDataStore = AppDataStore(context = application)
    private val repository: Repository = Repository(AppDatabase.getInstance(application))

    val user: StateFlow<UserTable?> = dataStore.loggedInUserIdFlow()
        .filterNotNull()
        .map { id -> repository.getUserById(id) } // Object
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val incomeAndExpenses: StateFlow<List<IncomeExpenseTable>> = user.filterNotNull()
        .flatMapLatest { user -> repository.getAllIncomeExpense(user.id) } // Flow
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())



    // TODO - State Flow of all income expenses added


}