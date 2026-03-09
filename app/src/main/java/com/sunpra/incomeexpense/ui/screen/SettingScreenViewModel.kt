package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDataStore
import com.sunpra.incomeexpense.model.AppLightOrDarkMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore: AppDataStore = AppDataStore(application)

    val appLightOrDarkMode : Flow<AppLightOrDarkMode?> = dataStore.appLightOrDarkModeFlow()

    fun onAppLightOrDarkModeChanged(appLightOrDarkMode: AppLightOrDarkMode) {
        viewModelScope.launch {
            dataStore.saveAppLightOrDarkMode(appLightOrDarkMode)
        }
    }

}