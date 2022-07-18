package com.bunbeauty.stories_compose.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val storyPreviewSize = 72.dp
val storyPreviewTextHeight = 16.dp

fun getStartPadding(index: Int): Dp = if (index == 0) 0.dp else 8.dp