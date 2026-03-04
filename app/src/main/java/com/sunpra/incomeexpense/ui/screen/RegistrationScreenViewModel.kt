package com.sunpra.incomeexpense.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunpra.incomeexpense.data.AppDatabase
import com.sunpra.incomeexpense.data.Repository
import com.sunpra.incomeexpense.data.ServiceProvider
import com.sunpra.incomeexpense.data.UserTable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.regex.Pattern

class RegistrationScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(ServiceProvider.tipsService, AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(RegistrationScreenUIState())
    val uiState = _uiState.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _navigateLogin = MutableSharedFlow<Unit>()
    val navigateLogin = _navigateLogin.asSharedFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()
    
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

    fun onConfirmPasswordChanged(value: String){
        _uiState.update { oldState ->
            oldState.copy(confirmPassword = value)
        }
    }

    fun onFullNameChanged(value: String){
        _uiState.update { oldState ->
            oldState.copy(fullName = value)
        }
    }

    fun hideMessage(){
        _message.update { null }
    }

    private fun validate(): Boolean {
        val uiState = _uiState.value
        val errors = mutableMapOf<String, String>()

        if(uiState.fullName.isEmpty()){
            errors["FullName"] = "Full name cannot be empty."
        }

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

        if(uiState.confirmPassword != uiState.password){
            errors["ConfirmPassword"] = "Password and confirm password doesn't match."
        }


        if (errors.isNotEmpty()) {
            _uiState.update { oldState ->
                oldState.copy(errors = errors)
            }
        }

        return errors.isEmpty()
    }

    fun onRegisterButtonClicked() {
        if (validate()) {
            val uiState: RegistrationScreenUIState = _uiState.value
            val id : String = UUID.randomUUID().toString()
            val fullName : String = uiState.fullName
            val email: String = uiState.email
            val password: String = uiState.password

            val userTable : UserTable = UserTable(id, fullName, email, password)

            viewModelScope.launch {
                // Register logic
                val result: Result<UserTable> = repository.registerUser(userTable)

                if(result.isFailure){
                    _message.update { result.exceptionOrNull()?.message }
                    return@launch
                }

                _toastMessage.emit("Registration success.")
                _navigateLogin.emit(Unit)
            }
        }
    }

}

data class RegistrationScreenUIState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errors: Map<String, String> = mapOf()
)