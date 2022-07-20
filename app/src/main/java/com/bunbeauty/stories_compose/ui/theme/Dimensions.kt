package com.bunbeauty.stories_compose.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val storyPreviewSize = 72.dp
val storyPreviewTextHeight = 16.dp

fun getStartPadding(index: Int, padding: Dp = 8.dp): Dp = if (index == 0) 0.dp else padding