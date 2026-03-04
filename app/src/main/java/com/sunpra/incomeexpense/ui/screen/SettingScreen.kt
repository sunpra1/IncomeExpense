package com.sunpra.incomeexpense.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.model.LightOrDarkMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navigateToLogin: () -> Unit,
    viewModel: SettingScreenViewModel = viewModel()
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val lightOrDarkMode by viewModel.lightOrDarkMode.collectAsStateWithLifecycle()
    val shortName by remember(user) {
        derivedStateOf {
            user?.fullName?.split(" ")
                ?.map { it.first() }
                ?.joinToString("") ?: ""
        }
    }


    LaunchedEffect(Unit) {
        launch {
            viewModel.navigateToLogin.collectLatest {
                navigateToLogin()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = "Setting",
                        style = MaterialTheme.typography.titleLarge
                    )
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
            Column {

                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(12.dp),
                            text = shortName,
                            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user?.fullName ?: "",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )

                            Text(
                                text = user?.email ?: "",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }

                        TextButton(onClick = viewModel::logoutUser) {
                            Text(
                                text = "LOGOUT",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp),
                    text = "Choose Light/Dark Mode",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )

                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 4.dp)
                        .align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LightOrDarkMode.entries.forEach {
                        FilterChip(
                            selected = lightOrDarkMode == it,
                            onClick = { viewModel.onLightOrDarkModeChanged(it) },
                            label = {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            leadingIcon = {
                                AnimatedVisibility(visible = lightOrDarkMode == it) {
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
        }
    }
}