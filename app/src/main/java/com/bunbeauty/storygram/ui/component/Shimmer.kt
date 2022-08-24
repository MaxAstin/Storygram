package com.bunbeauty.storygram.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.bunbeauty.storygram.ui.theme.Gray300
import com.bunbeauty.storygram.ui.theme.Gray400

@Composable
fun Shimmer(
    modifier: Modifier = Modifier
) {
    val translation = rememberInfiniteTransition()
    val translateAnimation = translation.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val shimmerColors = listOf(Gray400, Gray300, Gray400)
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
    Spacer(modifier = modifier.background(brush))
}

