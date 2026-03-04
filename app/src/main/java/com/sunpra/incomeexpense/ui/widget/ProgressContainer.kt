package com.sunpra.incomeexpense.ui.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


@Composable
fun ProgressContainer(
    showProgress: Boolean,
    content: @Composable BoxScope.() -> Unit
) {

    val blurRadius by animateDpAsState(
        targetValue = if (showProgress) 4.dp else 0.dp,
        animationSpec = tween(durationMillis = 150, easing = LinearEasing),
        label = "blurRadius"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(radius = blurRadius)
    ) {
        content()
    }

    if (showProgress)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput("Pointer Input") {} // This blocks all interactions
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(12.dp)
                    .align(Alignment.Center),
                strokeWidth = 4.dp
            )
        }
}