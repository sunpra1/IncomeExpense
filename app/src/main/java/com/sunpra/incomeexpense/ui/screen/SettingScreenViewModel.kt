package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDataStore
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.ServiceProvider
import com.sunpra.incomeexpense.model.LightOrDarkMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(ServiceProvider.tipsService, AppDatabase.getInstance(application))
    private val dataStore: AppDataStore = AppDataStore(application)

    private val _navigateToLogin = MutableSharedFlow<Unit>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    val user = dataStore.loggedInUserIdFlow().filterNotNull()
        .map { repository.getUserById(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val lightOrDarkMode = dataStore.lightOrDarkModeFlow().filterNotNull()
        .stateIn(viewModelScope, SharingStarted.Eagerly, LightOrDarkMode.System)


    fun onLightOrDarkModeChanged(lightOrDarkMode: LightOrDarkMode) {
        viewModelScope.launch {
            dataStore.saveLightOrDarkMode(lightOrDarkMode)
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            dataStore.saveLoggedInUserId(null)
            _navigateToLogin.emit(Unit)
        }
    }
}