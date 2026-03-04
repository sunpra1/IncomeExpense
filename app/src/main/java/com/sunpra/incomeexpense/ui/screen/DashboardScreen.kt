package com.sunpra.incomeexpense.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.data.IncomeExpense
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import com.sunpra.incomeexpense.data.exampleIncomeExpenseTable
import com.sunpra.incomeexpense.data.formattedDate
import com.sunpra.incomeexpense.model.IncomeExpenseAction
import com.sunpra.incomeexpense.model.TimeFilter
import com.sunpra.incomeexpense.ui.theme.IncomeExpenseTheme
import com.sunpra.incomeexpense.ui.widget.UpdateDailySavingDialog
import com.sunpra.incomeexpense.utility.to2d

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navigateToViewAllIncomeExpenses: () -> Unit,
    viewModel: DashboardScreenViewModel = viewModel()
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val addOrUpdateIncomeExpense by viewModel.addOrUpdateIncomeExpense.collectAsStateWithLifecycle()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredIncomeExpenses by viewModel.filteredIncomeExpense.collectAsStateWithLifecycle()

    val todayIncome by viewModel.todayIncome.collectAsStateWithLifecycle()
    val todayExpense by viewModel.todayExpense.collectAsStateWithLifecycle()
    val dailySavingGoalAchieved by viewModel.dailySavingGoalAchieved.collectAsStateWithLifecycle()

    val currentDailySavingGoal by viewModel.dailySavingGoal.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = "Welcome ${user?.fullName}",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = viewModel::addNewIncomeOrExpense) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
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
                item {
                    DailyOverviewSection(
                        modifier = Modifier.padding(12.dp),
                        todayIncome = todayIncome,
                        todayExpense = todayExpense,
                        dailySavingGoalAchieved = dailySavingGoalAchieved,
                        onUpdateDailySavingGoalClicked = viewModel::toggleShowUpdateDailySavingDialog
                    )
                }

                item {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Recent Logs",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )

                        TextButton(
                            onClick = navigateToViewAllIncomeExpenses
                        ) {
                            Text(
                                text = "View All",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Icon(
                                modifier = Modifier.size(12.dp),
                                painter = painterResource(R.drawable.chevron_right),
                                contentDescription = null
                            )
                        }
                    }
                }

                item {
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TimeFilter.entries.forEach {
                            FilterChip(
                                selected = uiState.timeFilter == it,
                                onClick = { viewModel.onTimeFilterChanged(it) },
                                label = {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                leadingIcon = {
                                    AnimatedVisibility(visible = uiState.timeFilter == it) {
                                        Icon(
                                            painter = painterResource(R.drawable.check_bold),
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Displaying",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )

                        AnimatedVisibility(uiState.incomeExpenseFilter == null) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "All",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                        }
                        AnimatedVisibility(uiState.incomeExpenseFilter != null) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(start = 12.dp, end = 4.dp)
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = uiState.incomeExpenseFilter?.name ?: "",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )

                                    Icon(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .clickable(
                                                onClick = {
                                                    viewModel.onIncomeExpenseFilterChanged(null)
                                                }
                                            )
                                            .padding(4.dp),
                                        painter = painterResource(R.drawable.close),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))


                        BoxWithConstraints bwc@{
                            Column {
                                Button(
                                    onClick = viewModel::toggleShowIncomeExpenseFilterOptions
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.filter),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Income/Expense",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                }


                                DropdownMenu(
                                    modifier = Modifier.width(this@bwc.maxWidth),
                                    expanded = uiState.showIncomeExpenseFilterOptions,
                                    onDismissRequest = viewModel::toggleShowIncomeExpenseFilterOptions
                                ) {
                                    IncomeExpense.entries.forEach {
                                        DropdownMenuItem(
                                            trailingIcon =
                                                if (it == uiState.incomeExpenseFilter) {
                                                    @Composable {
                                                        Icon(
                                                            modifier = Modifier.size(16.dp),
                                                            painter = painterResource(R.drawable.check_bold),
                                                            contentDescription = null
                                                        )
                                                    }
                                                } else null,
                                            text = {
                                                Text(
                                                    text = it.name,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                )
                                            },
                                            onClick = { viewModel.onIncomeExpenseFilterChanged(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                items(filteredIncomeExpenses) { item ->
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

    UpdateDailySavingDialog(
        showDialog = uiState.showUpdateDailySavingDialog,
        currentDailySavingAmount = currentDailySavingGoal,
        onDismissRequest = viewModel::toggleShowUpdateDailySavingDialog,
        onDailySavingAmountUpdated = viewModel::onDailySavingAmountUpdated
    )

    AddOrUpdateIncomeExpenseSheet(
        addOrUpdateData = addOrUpdateIncomeExpense,
        onDismissRequest = viewModel::dismissAddOrUpdateIncomeExpenseSheet
    )
}

@Composable
fun DailyOverviewSection(
    modifier: Modifier = Modifier,
    todayIncome: Double,
    todayExpense: Double,
    dailySavingGoalAchieved: Double,
    onUpdateDailySavingGoalClicked: () -> Unit
) {

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .shadow(
                    4.dp,
                    clip = false,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = MaterialTheme.colorScheme.onSurface
                )
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Daily Overview",
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = onUpdateDailySavingGoalClicked) {
                    Icon(
                        painter = painterResource(R.drawable.pencil_circle),
                        contentDescription = null
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 18.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .aspectRatio(1f),
                        progress = { dailySavingGoalAchieved.toFloat() },
                        strokeWidth = 12.dp,
                        trackColor = MaterialTheme.colorScheme.error
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Daily Goal Achieved",
                            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                        )

                        Text(
                            text = "${dailySavingGoalAchieved.to2d().times(100f)} %",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .border(
                                2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "Today's Income",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
                            )

                            Text(
                                text = "NRs. ${todayIncome.to2d()}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .padding(bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "Today's Expense",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
                            )

                            Text(
                                text = "NRs. ${todayExpense.to2d()}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDailyOverviewSection() {
    IncomeExpenseTheme {
        DailyOverviewSection(
            todayIncome = 1005.0,
            todayExpense = 100.0,
            dailySavingGoalAchieved = 0.5,
            onUpdateDailySavingGoalClicked = {}
        )
    }
}


@Composable
fun IncomeExpenseUI(
    modifier: Modifier = Modifier,
    incomeExpenseTable: IncomeExpenseTable,
    onIncomeExpenseActionSelected: (IncomeExpenseAction, IncomeExpenseTable) -> Unit
) {

    var showActions by remember { mutableStateOf(false) }

    fun toggleShowActions() {
        showActions = showActions.not()
    }

    fun handleIncomeExpenseActionSelection(incomeExpenseAction: IncomeExpenseAction) {
        showActions = false
        onIncomeExpenseActionSelected(incomeExpenseAction, incomeExpenseTable)
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            when (incomeExpenseTable.incomeExpense) {
                                IncomeExpense.Income -> R.drawable.plus_circle
                                IncomeExpense.Expense -> R.drawable.minus_circle
                            }
                        ),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .padding(end = 8.dp),
                        text = incomeExpenseTable.incomeExpense.name,
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .weight(1f)
                        .padding(end = 8.dp),
                    text = when (incomeExpenseTable.incomeExpense) {
                        IncomeExpense.Income -> incomeExpenseTable.incomeType?.name ?: ""
                        IncomeExpense.Expense -> incomeExpenseTable.expenseType?.name ?: ""
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Column {
                    IconButton(onClick = ::toggleShowActions) {
                        Icon(
                            painter = painterResource(R.drawable.dots_vertical),
                            contentDescription = null
                        )
                    }


                    DropdownMenu(
                        expanded = showActions,
                        onDismissRequest = ::toggleShowActions
                    ) {
                        IncomeExpenseAction.entries.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                },
                                onClick = { handleIncomeExpenseActionSelection(it) }
                            )
                        }
                    }
                }

            }

            Text(
                text = buildAnnotatedString {
                    append("Date Created: ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    ) {
                        append(incomeExpenseTable.formattedDate)
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 4.dp),
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 6.dp, bottom = 12.dp),
            ) {
                Text(
                    text = incomeExpenseTable.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer)
                )

                Text(
                    text = "NRs. ${incomeExpenseTable.amount}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewIncomeExpenseUI() {
    IncomeExpenseTheme {
        IncomeExpenseUI(
            incomeExpenseTable = exampleIncomeExpenseTable,
            onIncomeExpenseActionSelected = { _, _ -> }
        )
    }
}