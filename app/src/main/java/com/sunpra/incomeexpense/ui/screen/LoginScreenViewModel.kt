package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.UserTable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(LoginScreenUIState())
    val uiState = _uiState.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _navigateHome = MutableSharedFlow<Unit>()
    val navigateHome = _navigateHome.asSharedFlow()
    
    fun onEmailChanged(value: String) {
        _uiState.update { oldState ->
            oldState.copy(email = value)
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { oldState ->
            oldState.copy(password = value)
        }
    }

    fun hideMessage(){
        _message.update { null }
    }

    private fun validate(): Boolean {
        val uiState = _uiState.value
        val errors = mutableMapOf<String, String>()

        if (uiState.email.isEmpty()) {
            errors["Email"] = "Email cannot be empty."
        } else if (
            !Pattern.matches(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$",
                uiState.email
            )
        ) {
            errors["Email"] = "Invalid email provided."
        }
        if (uiState.password.isEmpty()) {
            errors["Password"] = "Password cannot be empty."
        } else if (uiState.password.length < 6) {
            errors["Password"] = "Password must be 6 characters long."
        }

        if (errors.isNotEmpty()) {
            _uiState.update { oldState ->
                oldState.copy(errors = errors)
            }
        }

        return errors.isEmpty()
    }

    fun onLoginButtonClicked() {
        if (validate()) {
            val uiState: LoginScreenUIState = _uiState.value
            val email: String = uiState.email
            val password: String = uiState.password

            viewModelScope.launch {
                val userTable: UserTable? = repository.login(email = email, password = password)
                if (userTable != null) {
                    // Email and password exits
                    // Navigate to Home screen
                    // TODO Save user id for home screen
                    _navigateHome.emit(Unit)
                } else {
                    // Email and password not available
                    // Show message
//                    _message.emit("Invalid email or password.")
                    _message.update { "Invalid email or password." }
                }
            }
        }
    }

}

data class LoginScreenUIState(
    val email: String = "",
    val password: String = "",
    val errors: Map<String, String> = mapOf()
)