package com.sunpra.incomeexpense.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sunpra.incomeexpense.model.LightOrDarkMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_data_store")

class AppDataStore(context: Context) {
    val dataStore = context.dataStore

    private val LOGGED_IN_USER_ID = stringPreferencesKey("logged_in_user_id")
    private val DAILY_SAVING_GOAL = doublePreferencesKey("daily_saving_goal")
    private val LIGHT_OR_DARK_MODE = stringPreferencesKey("light_or_dark")

    suspend fun saveLoggedInUserId(userId: String?) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                if(userId != null) {
                    preferences[LOGGED_IN_USER_ID] = userId
                }else{
                    preferences.remove(LOGGED_IN_USER_ID)
                }
            }
        }
    }

    fun loggedInUserIdFlow(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[LOGGED_IN_USER_ID]
    }

    suspend fun saveDailySavingGoal(savingGoalAmount: Double) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[DAILY_SAVING_GOAL] = savingGoalAmount
            }
        }
    }

    fun dailySavingGoalFlow(): Flow<Double?> = dataStore.data.map { preferences ->
        preferences[DAILY_SAVING_GOAL]
    }

    suspend fun saveLightOrDarkMode(lightOrDarkMode: LightOrDarkMode) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[LIGHT_OR_DARK_MODE] = lightOrDarkMode.name
            }
        }
    }

    fun lightOrDarkModeFlow(): Flow<LightOrDarkMode?> = dataStore.data.map { preferences ->
        val strMode = preferences[LIGHT_OR_DARK_MODE]
        if (strMode != null) LightOrDarkMode.valueOf(strMode)
         else null
    }
}