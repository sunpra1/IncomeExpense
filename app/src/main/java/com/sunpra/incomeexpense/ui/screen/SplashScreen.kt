package com.sunpra.incomeexpense.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.data.AppDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit
) {

    val context = LocalContext.current
    val dataStore = remember { AppDataStore(context) }

    LaunchedEffect(Unit) {
        delay(1.5.seconds)
        val userId = dataStore.loggedInUserIdFlow().first()
        if (userId == null) navigateToLogin()
        else navigateToHome()
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.statusBars)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(32.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.income_expense),
                    contentDescription = null
                )
            }

            Text(
                text = "Income And Expense",
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onTertiaryContainer)
            )

        }
    }
}