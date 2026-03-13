package com.sunpra.incomeexpense.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.data.IncomeOrExpense
import com.sunpra.incomeexpense.ui.widget.InputFieldError


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrUpdateIncomeOrExpenseScreen(
    viewModel: AddOrUpdateIncomeOrExpenseScreenViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(titleContentColor = MaterialTheme.colorScheme.primary),
                title = {
                    Text(
                        text = "Add Income Expense",
                        style = MaterialTheme.typography.titleLarge
                            .copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
            ) {

                FlowRow(
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .padding(top = 12.dp),
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp, alignment = Alignment.Start)
                ) {
                    IncomeOrExpense.entries.forEach { item ->
                        FilterChip(
                            selected = item == uiState.incomeOrExpense,
                            onClick = {
                                viewModel.onIncomeExpenseSelected(item)
                            },
                            label = {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            trailingIcon = {
                                AnimatedVisibility(visible = item == uiState.incomeOrExpense) {
                                    Icon(
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                }

                TextField(
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
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
                            text = "March Salary",
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontWeight = FontWeight.SemiBold)
                        )
                    },
                    value = uiState.name,
                    onValueChange = viewModel::onNameChanged
                )

                InputFieldError(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    errorText = uiState.errors["Name"]
                )


                TextField(
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = "Amount",
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontWeight = FontWeight.SemiBold)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "12000",
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
                    errorText = uiState.errors["Amount"]
                )
            }
        }
    }

}