package com.sunpra.incomeexpense.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sunpra.incomeexpense.R
import com.sunpra.incomeexpense.model.HealthTip
import com.sunpra.incomeexpense.model.exampleHealthTip
import com.sunpra.incomeexpense.ui.theme.IncomeExpenseTheme
import com.sunpra.incomeexpense.ui.widget.MessageDialog
import com.sunpra.incomeexpense.ui.widget.ProgressContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthTipsScreen(
    navigateToHealthTipDetailScreen: (HealthTip) -> Unit,
    viewModel: HealthTipsScreenViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = "Health Tips",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing.exclude(WindowInsets.navigationBars)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ProgressContainer(uiState.showProgress) {
                LazyColumn {
                    items(uiState.healthTips) { item ->
                        HealthTipUI(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .padding(top = 12.dp),
                            healthTip = item,
                            onClick = { navigateToHealthTipDetailScreen(item) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }

            MessageDialog(
                message = uiState.message,
                onDismissRequest = viewModel::hideMessage
            )
        }
    }
}


@Composable
fun HealthTipUI(
    modifier: Modifier = Modifier,
    healthTip: HealthTip,
    onClick: () -> Unit
) {

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = MaterialTheme.colorScheme.onSurface
                )
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.25f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(healthTip.image)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.image_placeholder),
                contentDescription = null
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .padding(end = 8.dp),
                    text = healthTip.title,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .padding(end = 8.dp),
                    text = healthTip.description,
                    style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                painter = painterResource(R.drawable.chevron_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHealthTipUI() {
    IncomeExpenseTheme {
        HealthTipUI(
            modifier = Modifier.padding(24.dp),
            healthTip = exampleHealthTip,
            onClick = {}
        )
    }
}
