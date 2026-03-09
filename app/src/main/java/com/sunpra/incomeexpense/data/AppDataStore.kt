package com.sunpra.incomeexpense.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sunpra.incomeexpense.model.AppLightOrDarkMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_data_store")

class AppDataStore(context: Context) {
    val dataStore = context.dataStore

    private val LOGGED_IN_USER_ID = stringPreferencesKey("logged_in_user_id")
    private val APP_LIGHT_OR_DARK_MODE = stringPreferencesKey("light_or_dark_mode")

    suspend fun saveLoggedInUserId(userId: String) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[LOGGED_IN_USER_ID] = userId
            }
        }
    }

    fun loggedInUserIdFlow(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[LOGGED_IN_USER_ID]
    }

    suspend fun saveAppLightOrDarkMode(appLightOrDarkMode: AppLightOrDarkMode) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[APP_LIGHT_OR_DARK_MODE] = appLightOrDarkMode.name
            }
        }
    }

    fun appLightOrDarkModeFlow(): Flow<AppLightOrDarkMode?> = dataStore.data.map { preferences ->
        val prefStr = preferences[APP_LIGHT_OR_DARK_MODE]
        if (prefStr != null) {
            AppLightOrDarkMode.valueOf(prefStr)
        } else {
            null
        }
    }
}