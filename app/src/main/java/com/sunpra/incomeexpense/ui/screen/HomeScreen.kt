package com.sunpra.incomeexpense.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.data.IncomeExpenseTable
import kotlinx.serialization.Serializable

@Serializable
data object InnerHomeRoute : NavKey

@Serializable
data object HealthTipRoute : NavKey

@Serializable
data object SettingRoute : NavKey

@Serializable
data class AddOrUpdateIncomeExpenseRoute(val incomeOrExpenseTable: IncomeExpenseTable?) : NavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreen() {
    val backStack = rememberNavBackStack(InnerHomeRoute)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val isTablet by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
        }
    }

    Row {

        AnimatedVisibility(visible = isTablet) {
            NavigationRail {

                NavigationRailItem(
                    selected = backStack.last() == InnerHomeRoute,
                    onClick = {
                        backStack.clear()
                        backStack.add(InnerHomeRoute)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.home),
                            contentDescription = "Home tab icon."
                        )
                    },
                    label = {
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                )

                NavigationRailItem(
                    selected = backStack.last() == HealthTipRoute,
                    onClick = {
                        backStack.clear()
                        backStack.add(HealthTipRoute)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.heart),
                            contentDescription = "Home tab icon."
                        )
                    },
                    label = {
                        Text(
                            text = "Health Tips",
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                )

                NavigationRailItem(
                    selected = backStack.last() == SettingRoute,
                    onClick = {
                        backStack.clear()
                        backStack.add(SettingRoute)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.cog),
                            contentDescription = "Home tab icon."
                        )
                    },
                    label = {
                        Text(
                            text = "Setting",
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                )

            }
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) {

            NavDisplay(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                sceneStrategy = listDetailStrategy,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator()
                ),
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<InnerHomeRoute>(
                        metadata = ListDetailSceneStrategy.listPane(
                            detailPlaceholder = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Please add/update new income expense.",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        )
                    ) {
                        InnerHomeScreen(
                            navigateToAddOrUpdateIncomeOrExpenseScreen = {
                                backStack.add(AddOrUpdateIncomeExpenseRoute(it))
                            }
                        )
                    }
                    entry<AddOrUpdateIncomeExpenseRoute>(
                        metadata = ListDetailSceneStrategy.detailPane()
                    ) {
                        AddOrUpdateIncomeOrExpenseScreen(
                            incomeExpenseTable = it.incomeOrExpenseTable,
                            navigateToHomeScreen = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                    entry<HealthTipRoute> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(text = "This is health tip screen.")
                        }
                    }
                    entry<SettingRoute> {
                        SettingScreen()
                    }
                }
            )

            AnimatedVisibility(visible = isTablet.not()) {
                NavigationBar {

                    NavigationBarItem(
                        selected = backStack.last() == InnerHomeRoute,
                        onClick = {
                            backStack.clear()
                            backStack.add(InnerHomeRoute)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.home),
                                contentDescription = "Home tab icon."
                            )
                        },
                        label = {
                            Text(
                                text = "Home",
                                style = MaterialTheme.typography.bodyMedium
                                    .copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = backStack.last() == HealthTipRoute,
                        onClick = {
                            backStack.clear()
                            backStack.add(HealthTipRoute)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.heart),
                                contentDescription = "Home tab icon."
                            )
                        },
                        label = {
                            Text(
                                text = "Health Tips",
                                style = MaterialTheme.typography.bodyMedium
                                    .copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = backStack.last() == SettingRoute,
                        onClick = {
                            backStack.clear()
                            backStack.add(SettingRoute)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.cog),
                                contentDescription = "Home tab icon."
                            )
                        },
                        label = {
                            Text(
                                text = "Setting",
                                style = MaterialTheme.typography.bodyMedium
                                    .copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    )

                }
            }
        }
    }
}