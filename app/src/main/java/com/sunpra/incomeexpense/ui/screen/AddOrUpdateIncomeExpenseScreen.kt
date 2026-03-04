package com.sunpra.incomeexpense.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.data.ExpenseType
import com.sunpra.incomeexpense.data.IncomeExpense
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import com.sunpra.incomeexpense.data.IncomeType
import com.sunpra.incomeexpense.ui.widget.InputFieldError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class AddOrUpdate {
    Add, Update
}

data class AddOrUpdateData(
    val addOrUpdate: AddOrUpdate,
    val incomeExpense: IncomeExpenseTable?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrUpdateIncomeExpenseSheet(
    addOrUpdateData: AddOrUpdateData?,
    onDismissRequest: () -> Unit
) {

    if (addOrUpdateData != null) {

        val coroutine = rememberCoroutineScope()

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        fun handleDismissRequest() {
            coroutine.launch {
                sheetState.hide()
            }.invokeOnCompletion { onDismissRequest() }
        }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = ::handleDismissRequest
        ) {
            AddOrUpdateIncomeExpenseScreen(
                addOrUpdateData = addOrUpdateData,
                onDismissRequest = ::handleDismissRequest
            )
        }
    }

}

@Composable
fun AddOrUpdateIncomeExpenseScreen(
    modifier: Modifier = Modifier,
    addOrUpdateData: AddOrUpdateData,
    onDismissRequest: () -> Unit,
    viewModel: AddOrUpdateIncomeExpenseScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleAddOrUpdateData(addOrUpdateData)

        launch {
            viewModel.dismissSheet.collectLatest {
                onDismissRequest()
            }
        }
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = when (addOrUpdateData.addOrUpdate) {
                AddOrUpdate.Add -> "Add New Income or Expense"
                AddOrUpdate.Update -> "Update Your Income or Expense"
            },
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 18.dp))

        FlowRow(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IncomeExpense.entries.forEach {
                FilterChip(
                    selected = uiState.incomeExpense == it,
                    onClick = { viewModel.onIncomeExpenseChosen(it) },
                    label = {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        AnimatedVisibility(visible = uiState.incomeExpense == it) {
                            Icon(
                                painter = painterResource(R.drawable.check_bold),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }

        TextField(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp)
                .fillMaxWidth(),
            label = {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                )
            },
            placeholder = {
                Text(
                    text = "March salary",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                )
            },
            value = uiState.name,
            onValueChange = viewModel::onNameChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        InputFieldError(
            modifier = Modifier.padding(horizontal = 12.dp),
            errorText = uiState.formErrors["Name"]
        )

        TextField(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp)
                .fillMaxWidth(),
            prefix = {
                Text(
                    text = "NRs.",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(fontWeight = FontWeight.Bold)
                )
            },
            label = {
                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                )
            },
            placeholder = {
                Text(
                    text = "12.99",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                )
            },
            value = uiState.amount,
            onValueChange = viewModel::onAmountChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        InputFieldError(
            modifier = Modifier.padding(horizontal = 12.dp),
            errorText = uiState.formErrors["Amount"]
        )

        AnimatedVisibility(visible = uiState.incomeExpense == IncomeExpense.Income) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 16.dp),
                    text = "Income Type",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.tertiaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = viewModel::toggleIncomeTypeDropdown)
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = uiState.incomeType?.name ?: "Select Income Type",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                DropdownMenu(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    expanded = uiState.showIncomeTypeDropdown,
                    onDismissRequest = viewModel::toggleIncomeTypeDropdown
                ) {
                    IncomeType.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyMedium
                                        .copy(fontWeight = FontWeight.SemiBold)
                                )
                            },
                            onClick = { viewModel.onIncomeTypeSelected(it) }
                        )
                    }
                }

                InputFieldError(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    errorText = uiState.formErrors["IncomeType"]
                )
            }
        }

        AnimatedVisibility(visible = uiState.incomeExpense == IncomeExpense.Expense) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 16.dp),
                    text = "Expense Type",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                )
                Box(
                    Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.tertiaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = viewModel::toggleExpenseTypeDropdown)
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = uiState.expenseType?.name ?: "Select Expense Type",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                DropdownMenu(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    expanded = uiState.showExpenseTypeDropdown,
                    onDismissRequest = viewModel::toggleExpenseTypeDropdown
                ) {
                    ExpenseType.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyMedium
                                        .copy(fontWeight = FontWeight.SemiBold)
                                )
                            },
                            onClick = { viewModel.onExpenseTypeSelected(it) }
                        )
                    }
                }

                InputFieldError(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    errorText = uiState.formErrors["ExpenseType"]
                )
            }

        }


        Button(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 24.dp)
                .heightIn(min = 56.dp)
                .fillMaxWidth(),
            onClick = viewModel::onSubmitButtonClicked
        ) {
            Text(
                text = "Submit",
                style = MaterialTheme.typography.bodyLarge
                    .copy(fontWeight = FontWeight.Bold)
            )
        }
    }

}