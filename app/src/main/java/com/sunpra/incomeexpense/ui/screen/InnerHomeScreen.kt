package com.sunpra.incomeexpense.ui.screen

import android.icu.util.Calendar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.model.TimeFilterOption
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InnerHomeScreen(
    navigateToAddOrUpdateIncomeOrExpenseScreen: () -> Unit,
    viewModel: InnerHomeScreenViewModel = viewModel()
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val incomeAndExpenses by viewModel.incomeAndExpenses.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = "Welcome ${user?.fullName}",
                        style = MaterialTheme.typography.titleLarge
                            .copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                },
                actions = {
                    IconButton(
                        onClick = navigateToAddOrUpdateIncomeOrExpenseScreen
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {

            Column(modifier = Modifier.fillMaxSize()) {
                FlowRow(
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    TimeFilterOption.entries.forEach { timeFilterOption ->
                        FilterChip(
                            selected = uiState.timeFilterOption == timeFilterOption,
                            onClick = {
                                viewModel.onTimeFilterOptionChanged(timeFilterOption)
                            },
                            label = {
                                Text(
                                    text = timeFilterOption.name,
                                    style = MaterialTheme.typography.bodyMedium
                                        .copy(fontWeight = FontWeight.SemiBold)
                                )
                            },
                            leadingIcon = {
                                AnimatedVisibility(visible = uiState.timeFilterOption == timeFilterOption) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = "Selected Check Icon"
                                    )
                                }
                            }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(incomeAndExpenses) { item ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.incomeOrExpense.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = getReadableDateFromMillis(item.dateCreated),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "NRs. ${item.amount}",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = item.incomeType?.name ?: item.expenseType?.name ?: "",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getReadableDateFromMillis(millis: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
    val dateFormat = SimpleDateFormat("hh:ss a, MMM dd, yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}