package com.sunpra.incomeexpense.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_data_store")

class AppDataStore(context: Context) {
    val dataStore = context.dataStore

    private val LOGGED_IN_USER_ID = stringPreferencesKey("logged_in_user_id")

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
}