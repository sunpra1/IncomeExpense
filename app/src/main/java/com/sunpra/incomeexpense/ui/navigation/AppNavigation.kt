package com.sunpra.incomeexpense.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sunpra.incomeexpense.ui.screen.HomeScreen
import com.sunpra.incomeexpense.ui.screen.LoginScreen
import com.sunpra.incomeexpense.ui.screen.RegistrationScreen
import com.sunpra.incomeexpense.ui.screen.SplashScreen
import kotlinx.serialization.Serializable


@Serializable
data object SplashRoute : NavKey

@Serializable
data object LoginRoute : NavKey

@Serializable
data object RegistrationRoute : NavKey

@Serializable
data object HomeRoute : NavKey

@Composable
fun AppNavigation() {

    val backStack = rememberNavBackStack(SplashRoute)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator()
        ),
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<SplashRoute> {
                SplashScreen(
                    navigateToLogin = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    },
                    navigateToHome = {
                        backStack.clear()
                        backStack.add(HomeRoute)
                    }
                )
            }

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
                    navigateBack = {
                        backStack.removeLastOrNull()
                    },
                    navigateToLogin = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    }
                )
            }

            entry<HomeRoute> {
                HomeScreen(
                    navigateToLogin = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    }
                )
            }
        }
    )
}