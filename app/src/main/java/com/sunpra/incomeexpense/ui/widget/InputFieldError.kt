package com.sunpra.incomeexpense.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InputFieldError(
    modifier: Modifier = Modifier,
    errorText: String?
) {
    AnimatedVisibility(
        visible = errorText != null,
        enter = slideInVertically { -it } + fadeIn(),
        exit = fadeOut() + slideOutVertically { -it }
    ) {
        Text(
            modifier = modifier,
            text = errorText ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.error
            )
        )
    }
}