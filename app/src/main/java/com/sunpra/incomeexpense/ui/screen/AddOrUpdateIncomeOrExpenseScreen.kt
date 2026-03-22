package com.sunpra.incomeexpense.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.data.ExpenseType
import com.sunpra.incomeexpense.data.IncomeOrExpense
import com.sunpra.incomeexpense.data.IncomeType
import com.sunpra.incomeexpense.ui.widget.InputFieldError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrUpdateIncomeOrExpenseScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: AddOrUpdateIncomeOrExpenseScreenViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        launch {
            viewModel.navigateToHomeScreen.collectLatest {
                navigateToHomeScreen()
            }
        }
    }

    BackHandler(onBack = navigateToHomeScreen)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {

                    IconButton(
                        onClick = navigateToHomeScreen
                    ) {
                        Text("Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
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
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
            ) {

                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
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
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
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
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
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

                AnimatedVisibility(visible = uiState.incomeOrExpense == IncomeOrExpense.Income) {
                    Column {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .padding(top = 12.dp)
                                .sizeIn(minHeight = 52.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable(onClick = viewModel::toggleIncomeTypeDropDown)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = uiState.incomeType?.name ?: "Select Income Type",
                                    style = MaterialTheme.typography.bodyMedium
                                        .copy(fontWeight = FontWeight.SemiBold)
                                )

                                val rotationDegree by animateFloatAsState(
                                    targetValue = if (uiState.showIncomeTypeDropdown) 0f else 180f,
                                    label = "rotationDegree"
                                )

                                Icon(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .rotate(rotationDegree),
                                    painter = painterResource(R.drawable.triangle),
                                    contentDescription = "icon_triangle"
                                )
                            }

                            DropdownMenu(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                expanded = uiState.showIncomeTypeDropdown,
                                onDismissRequest = viewModel::toggleIncomeTypeDropDown
                            ) {
                                IncomeType.entries.forEach { item ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = item.name,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        onClick = { viewModel.onIncomeTypeSelected(item) }
                                    )
                                }
                            }
                        }

                        InputFieldError(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            errorText = uiState.errors["IncomeType"]
                        )
                    }
                }

                AnimatedVisibility(visible = uiState.incomeOrExpense == IncomeOrExpense.Expense) {
                    Column {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .padding(top = 12.dp)
                                .sizeIn(minHeight = 52.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable(onClick = viewModel::toggleExpenseTypeDropDown)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = uiState.expenseType?.name ?: "Select Expense Type",
                                    style = MaterialTheme.typography.bodyMedium
                                        .copy(fontWeight = FontWeight.SemiBold)
                                )

                                val rotationDegree by animateFloatAsState(
                                    targetValue = if (uiState.showExpenseTypeDropdown) 0f else 180f,
                                    label = "rotationDegree"
                                )

                                Icon(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .rotate(rotationDegree),
                                    painter = painterResource(R.drawable.triangle),
                                    contentDescription = "icon_triangle"
                                )
                            }

                            DropdownMenu(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                expanded = uiState.showExpenseTypeDropdown,
                                onDismissRequest = viewModel::toggleExpenseTypeDropDown
                            ) {
                                ExpenseType.entries.forEach { item ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = item.name,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        onClick = { viewModel.onExpenseTypeSelected(item) }
                                    )
                                }
                            }
                        }

                        InputFieldError(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            errorText = uiState.errors["ExpenseType"]
                        )
                    }
                }


                TextField(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = "Note (Optional)",
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontWeight = FontWeight.SemiBold)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Your Note",
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontWeight = FontWeight.SemiBold)
                        )
                    },
                    value = uiState.note,
                    onValueChange = viewModel::onNoteChanged,
                    minLines = 8
                )

                Button(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 32.dp),
                    onClick = viewModel::onFormSubmitted
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }

}