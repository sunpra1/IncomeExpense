package com.sunpra.incomeexpense.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.ui.navigation.HomeRoute
import com.sunpra.incomeexpense.ui.navigation.LoginRoute
import com.sunpra.incomeexpense.ui.navigation.RegistrationRoute
import kotlinx.serialization.Serializable

@Serializable
data object InnerHomeRoute // Dashboard

@Serializable
data object HealthTipRoute

@Serializable
data object SettingRoute

@Composable
fun HomeScreen() {

    val backStack = remember { mutableStateListOf<Any>(InnerHomeRoute) }

    Column(modifier = Modifier.fillMaxSize()) {

        NavDisplay(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<InnerHomeRoute> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {

                        Text( text = "This is Home screen.")
                    }
                }
                entry<HealthTipRoute> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {

                        Text( text = "This is health tip screen.")
                    }
                }
                entry<SettingRoute> {
                    SettingScreen()
                }
            }
        )

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