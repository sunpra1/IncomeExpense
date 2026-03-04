package com.sunpra.incomeexpense.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun HealthTipDetailScreen(
    navigateBack: () -> Unit,
    healthTip: HealthTip
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.chevron_left),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = "Health Tips",
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
                AsyncImage(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .aspectRatio(1.2f)
                        .clip(RoundedCornerShape(4.dp)),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(healthTip.image)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.image_placeholder),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = healthTip.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    modifier = Modifier.padding(12.dp),
                    text = healthTip.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
