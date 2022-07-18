package com.bunbeauty.stories_compose.ui.screen

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.stories_compose.model.Story
import com.bunbeauty.stories_compose.model.StoryDetails
import com.bunbeauty.stories_compose.model.StoryState
import com.bunbeauty.stories_compose.ui.theme.getStartPadding

@Composable
fun StoryDetailsScreen() {
    val storyDetailsState = remember {
        mutableStateOf(
            StoryDetails(
                name = "story #$0",
                storyList = listOf(
                    Story(
                        state = StoryState.IN_PROGRESS,
                        link = "https://picsum.photos/100"
                    ),
                    Story(
                        state = StoryState.NOT_SHOWN,
                        link = "https://picsum.photos/100"
                    ),
                    Story(
                        state = StoryState.NOT_SHOWN,
                        link = "https://picsum.photos/100"
                    ),
                    Story(
                        state = StoryState.NOT_SHOWN,
                        link = "https://picsum.photos/100"
                    )
                )
            )
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        storyDetailsState.value.storyList.forEachIndexed { index, story ->
            StoryProgressIndicator(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = getStartPadding(index)),
                storyState = story.state,
            ) {
                Log.d("testTag", "$index finish")
                storyDetailsState.value = storyDetailsState.value.copy(
                    storyList = nextStory(storyDetailsState.value.storyList)
                )
            }
        }
    }
}

private fun nextStory(storyList: List<Story>): List<Story> {
    val newStoryList = mutableListOf<Story>()
    storyList.forEachIndexed { i, story ->
        when (story.state) {
            StoryState.SHOWN -> {
                newStoryList.add(story)
            }
            StoryState.IN_PROGRESS -> {
                newStoryList.add(story.copy(state = StoryState.SHOWN))
            }
            StoryState.NOT_SHOWN -> {
                if (i == 0 || storyList[i - 1].state == StoryState.IN_PROGRESS) {
                    newStoryList.add(story.copy(state = StoryState.IN_PROGRESS))
                } else {
                    newStoryList.add(story)
                }
            }
        }
    }
    Log.d("testTag", "newStoryList ${newStoryList.joinToString()}")
    return newStoryList.toList()
}

@Composable
private fun StoryProgressIndicator(
    modifier: Modifier = Modifier,
    storyState: StoryState,
    finishedListener: (() -> Unit)
) {
    val progressState = remember {
        mutableStateOf(
            if (storyState == StoryState.SHOWN) 1f else 0f
        )
    }
    val progressAnimationValue by animateFloatAsState(
        targetValue = progressState.value,
        animationSpec = tween(durationMillis = 5_000, easing = LinearEasing)
    ) {
        if (storyState == StoryState.IN_PROGRESS) {
            finishedListener()
        }
    }
    LinearProgressIndicator(
        modifier = modifier,
        progress = progressAnimationValue
    )
    if (storyState == StoryState.IN_PROGRESS) {
        SideEffect {
            progressState.value = 1f
        }
    }
}

@Composable
@Preview
fun StoryDetailsScreenPreview() {
    StoryDetailsScreen()
}