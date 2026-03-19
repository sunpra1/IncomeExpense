package com.sunpra.incomeexpense.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sunpra.incomeexpense.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InnerHomeScreen(
    navigateToAddOrUpdateIncomeOrExpenseScreen: () -> Unit
) {
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
                        text = "Welcome User",
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

        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

        }
    }

}