package com.bunbeauty.stories_compose.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bunbeauty.stories_compose.model.StoryState
import com.bunbeauty.stories_compose.ui.theme.Gray400
import com.bunbeauty.stories_compose.ui.theme.White

private const val PROGRESS_START_VALUE = 0f
private const val PROGRESS_FINISH_VALUE = 1f
private const val PLAY_TIME_START_MILLIS = 0L
private const val PLAY_TIME_FINISH_MILLIS = 5_000

@Composable
fun StoryProgressIndicator(
    modifier: Modifier = Modifier,
    storyState: StoryState,
    isPaused: Boolean,
    finishedListener: (() -> Unit),
) {
    val animation = remember {
        TargetBasedAnimation(
            animationSpec = tween(durationMillis = PLAY_TIME_FINISH_MILLIS, easing = LinearEasing),
            typeConverter = Float.VectorConverter,
            initialValue = PROGRESS_START_VALUE,
            targetValue = PROGRESS_FINISH_VALUE
        )
    }
    var playTime by remember { mutableStateOf(PLAY_TIME_START_MILLIS) }
    var progressAnimationValue by remember { mutableStateOf(PROGRESS_START_VALUE) }

    if (storyState == StoryState.IN_PROGRESS) {
        LaunchedEffect(isPaused) {
            val startTime = withFrameNanos { it } - playTime
            while (!isPaused && (progressAnimationValue != PROGRESS_FINISH_VALUE)) {
                playTime = withFrameNanos { it } - startTime
                progressAnimationValue = animation.getValueFromNanos(playTime)
                if (progressAnimationValue == PROGRESS_FINISH_VALUE) {
                    finishedListener()
                }
            }
        }
    } else {
        playTime = PLAY_TIME_START_MILLIS
        progressAnimationValue = PROGRESS_START_VALUE
    }

    val progress = when (storyState) {
        StoryState.SHOWN -> PROGRESS_FINISH_VALUE
        StoryState.NOT_SHOWN -> PROGRESS_START_VALUE
        StoryState.IN_PROGRESS -> progressAnimationValue
    }
    LinearProgressIndicator(
        modifier = modifier
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp)),
        color = White,
        progress = progress
    )
}