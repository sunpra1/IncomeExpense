package com.sunpra.incomeexpense.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunpra.incomeexpense.ui.theme.IncomeExpenseTheme
import com.sunpra.incomeexpense.ui.widget.InputFieldError
import com.sunpra.incomeexpense.ui.widget.MessageDialog

@Composable
fun RegistrationScreen(
    viewModel : RegistrationScreenViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()

    Scaffold() { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).animateContentSize()) {
            TextField(
                modifier = Modifier.padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Full Name",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                placeholder = {
                    Text(
                        text = "John Doe",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                value = uiState.fullName,
                onValueChange = viewModel::onFullNameChanged
            )
            InputFieldError(
                modifier = Modifier.padding(horizontal = 12.dp),
                errorText = uiState.errors["FullName"]
            )

            TextField(
                modifier = Modifier.padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                placeholder = {
                    Text(
                        text = "john_doe@abc.com",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged
            )

            InputFieldError(
                modifier = Modifier.padding(horizontal = 12.dp),
                errorText = uiState.errors["Email"]
            )

            TextField(
                modifier = Modifier.padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                placeholder = {
                    Text(
                        text = "***********",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged
            )
            InputFieldError(
                modifier = Modifier.padding(horizontal = 12.dp),
                errorText = uiState.errors["Password"]
            )

            TextField(
                modifier = Modifier.padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Confirm Password",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                placeholder = {
                    Text(
                        text = "***********",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChanged
            )
            InputFieldError(
                modifier = Modifier.padding(horizontal = 12.dp),
                errorText = uiState.errors["ConfirmPassword"]
            )

            Button(
                modifier = Modifier.padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                onClick = viewModel::onRegisterButtonClicked
            ) {
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                )
            }

        }

        MessageDialog(
            message = message,
            onDismissRequest = viewModel::hideMessage
        )
    }
}

@Preview
@Composable
fun PreviewRegistrationScreen(){
    IncomeExpenseTheme {
        RegistrationScreen()
    }
}