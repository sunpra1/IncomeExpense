package com.sunpra.incomeexpense.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sunpra.incomeexpense.ui.screen.HomeScreen
import com.sunpra.incomeexpense.ui.screen.LoginScreen
import com.sunpra.incomeexpense.ui.screen.RegistrationScreen
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

@Serializable
data object RegistrationRoute

@Serializable
data object HomeRoute

@Composable
fun AppNavigation() {

    val backStack = remember { mutableStateListOf<Any>(LoginRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<LoginRoute> {
                LoginScreen(
                    navigateToRegister = {
                        backStack.add(RegistrationRoute)
                    },
                    navigateToHome = {
                        backStack.clear()
                        backStack.add(HomeRoute)
                    }
                )
            }

            entry<RegistrationRoute> {
                RegistrationScreen(
                    navigateToLogin = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    }
                )
            }

            entry<HomeRoute> {
                HomeScreen()
            }
        }
    )

}