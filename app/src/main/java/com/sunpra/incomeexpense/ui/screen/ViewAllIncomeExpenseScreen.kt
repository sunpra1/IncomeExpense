package com.sunpra.incomeexpense.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunpra.incomeexpense.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllIncomeExpenseScreen(
    navigateBack: () -> Unit,
    viewModel: ViewAllIncomeExpenseScreenViewModel = viewModel()
) {
    val addOrUpdateIncomeExpense by viewModel.addOrUpdateIncomeExpense.collectAsStateWithLifecycle()

    val incomeExpenses by viewModel.incomeExpenses.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.chevron_left),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = "All Income and Expense Logs",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
        contentWindowInsets =  WindowInsets.safeDrawing.exclude(WindowInsets.navigationBars)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn {
                items(incomeExpenses) { item ->
                    IncomeExpenseUI(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(top = 12.dp),
                        incomeExpenseTable = item,
                        onIncomeExpenseActionSelected = viewModel::onIncomeExpenseActionSelected
                    )
                }
            }
        }
    }

    AddOrUpdateIncomeExpenseSheet(
        addOrUpdateData = addOrUpdateIncomeExpense,
        onDismissRequest = viewModel::dismissAddOrUpdateIncomeExpenseSheet
    )
}