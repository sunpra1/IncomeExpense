package com.sunpra.incomeexpense.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.sunpra.incomeexpense.model.HealthTip
import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute : NavKey

@Serializable
data object HealthTipsRoute : NavKey

@Serializable
data class HealthTipDetailRoute(val healthTip: HealthTip) : NavKey

@Serializable
data object SettingRoute : NavKey

@Serializable
data object ViewAllIncomeExpenseRoute : NavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit
) {

    val backStack = rememberNavBackStack(DashboardRoute)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    Row {

        AnimatedVisibility(
            visible = windowAdaptiveInfo.windowSizeClass
                .isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
        ) {
            NavigationRail {
                NavigationRailItem(
                    selected = backStack.last() == DashboardRoute,
                    onClick = {
                        backStack.clear()
                        backStack.add(DashboardRoute)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.home),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                )

                NavigationRailItem(
                    selected = backStack.last() == HealthTipsRoute,
                    onClick = {
                        backStack.clear()
                        backStack.add(HealthTipsRoute)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.health),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = "Health Tips",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
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
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = "Setting",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            NavDisplay(
                modifier = Modifier.weight(1f),
                backStack = backStack,
                sceneStrategy = listDetailStrategy,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator()
                ),
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<DashboardRoute>(
                        metadata = ListDetailSceneStrategy.listPane(
                            detailPlaceholder = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Click on view all button to view all income expense logs.",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        )
                    ) {
                        DashboardScreen(
                            navigateToViewAllIncomeExpenses = {
                                backStack.add(ViewAllIncomeExpenseRoute)
                            }
                        )
                    }

                    entry<HealthTipsRoute>(
                        metadata = ListDetailSceneStrategy.listPane(
                            detailPlaceholder = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Click on health tip to view.",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        )
                    ) {
                        HealthTipsScreen(
                            navigateToHealthTipDetailScreen = { healthTip ->
                                backStack.add(
                                    HealthTipDetailRoute(healthTip)
                                )
                            }
                        )
                    }

                    entry<HealthTipDetailRoute>(
                        metadata = ListDetailSceneStrategy.detailPane()
                    ) {
                        HealthTipDetailScreen(
                            healthTip = it.healthTip,
                            navigateBack = { backStack.removeLastOrNull() }
                        )
                    }

                    entry<ViewAllIncomeExpenseRoute>(
                        metadata = ListDetailSceneStrategy.detailPane()
                    ) {
                        ViewAllIncomeExpenseScreen(
                            navigateBack = { backStack.removeLastOrNull() }
                        )
                    }

                    entry<SettingRoute> {
                        SettingScreen(
                            navigateToLogin = navigateToLogin
                        )
                    }
                }
            )
            AnimatedVisibility(
                visible = windowAdaptiveInfo.windowSizeClass
                    .isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND).not()
            ) {
                NavigationBar {
                    NavigationBarItem(
                        selected = backStack.last() == DashboardRoute || backStack.last() == ViewAllIncomeExpenseRoute,
                        onClick = {
                            backStack.clear()
                            backStack.add(DashboardRoute)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.home),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = "Home",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = backStack.last() == HealthTipsRoute || backStack.last() == HealthTipDetailRoute,
                        onClick = {
                            backStack.clear()
                            backStack.add(HealthTipsRoute)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.health),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = "Health Tips",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
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
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = "Setting",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    )
                }
            }
        }
    }
}