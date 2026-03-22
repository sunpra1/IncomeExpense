package com.sunpra.incomeexpense.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sunpra.incomeexpense.ui.screen.HomeScreen
import com.sunpra.incomeexpense.ui.screen.LoginScreen
import com.sunpra.incomeexpense.ui.screen.RegistrationScreen
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute: NavKey

@Serializable
data object RegistrationRoute: NavKey

@Serializable
data object HomeRoute: NavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation() {

    val backStack = rememberNavBackStack(LoginRoute)

    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator()
        ),
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<LoginRoute>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Box(modifier = Modifier.fillMaxSize())
                    }
                )
            ) {
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

            entry<RegistrationRoute>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
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