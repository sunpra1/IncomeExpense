package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.ServiceProvider
import com.sunpra.incomeexpense.model.HealthTip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HealthTipsScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository =
        Repository(ServiceProvider.tipsService, AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(HealthTipsScreenUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getHealthTips()
    }

    private fun getHealthTips() {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(showProgress = true)
            }

            val result: Result<List<HealthTip>> = repository.getHealthTips()

            if (result.isSuccess) {
                val memories: List<HealthTip> = result.getOrThrow()
                _uiState.update { oldState ->
                    oldState.copy(
                        healthTips = memories.toMutableStateList(),
                        showProgress = false
                    )
                }
            } else {
                _uiState.update { oldState ->
                    oldState.copy(
                        message = result.exceptionOrNull()?.message ?: "Something went wrong",
                        showProgress = false
                    )
                }
            }
        }
    }

    fun hideMessage() {
        _uiState.update { oldState ->
            oldState.copy(message = null)
        }
    }
}


data class HealthTipsScreenUIState(
    val healthTips: SnapshotStateList<HealthTip> = mutableStateListOf(),
    val showProgress: Boolean = false,
    val message: String? = null
)
