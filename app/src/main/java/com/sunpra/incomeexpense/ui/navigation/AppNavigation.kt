package com.sunpra.incomeexpense.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sunpra.incomeexpense.ui.screen.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

@Serializable
data object RegistrationRoute

@Composable
fun AppNavigation() {

    val backStack = remember { mutableStateListOf<Any>(LoginRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<LoginRoute> {
                LoginScreen()
            }

            entry<RegistrationRoute> {

            }
        }
    )

}