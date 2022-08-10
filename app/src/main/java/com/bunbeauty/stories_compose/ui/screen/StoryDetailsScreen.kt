package com.bunbeauty.stories_compose.ui.screen

import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bunbeauty.stories_compose.model.Story
import com.bunbeauty.stories_compose.model.StoryDetails
import com.bunbeauty.stories_compose.model.StoryGroup
import com.bunbeauty.stories_compose.model.StoryState
import com.bunbeauty.stories_compose.ui.component.StoryProgressIndicator
import com.bunbeauty.stories_compose.ui.theme.getStartPadding

private const val MAX_TIME_FOR_CLICK = 300L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StoryDetailsScreen() {
    var storyDetails by remember {
        mutableStateOf(
            StoryDetails(
                storyGroupList = listOf(
                    StoryGroup(
                        groupId = 1,
                        name = "story#1",
                        previewLink = "https://picsum.photos/100",
                        isCurrent = true,
                        storyList = listOf(
                            Story(
                                id = 0,
                                state = StoryState.IN_PROGRESS,
                                link = "https://picsum.photos/500"
                            ),
                            Story(
                                id = 1,
                                state = StoryState.NOT_SHOWN,
                                link = "https://picsum.photos/500"
                            ),
                            Story(
                                id = 2,
                                state = StoryState.NOT_SHOWN,
                                link = "https://picsum.photos/500"
                            ),
                            Story(
                                id = 3,
                                state = StoryState.NOT_SHOWN,
                                link = "https://picsum.photos/500"
                            ),
                            Story(
                                id = 4,
                                state = StoryState.NOT_SHOWN,
                                link = "https://picsum.photos/500"
                            ),
                        )
                    ),
                    StoryGroup(
                        groupId = 2,
                        name = "story#2",
                        previewLink = "https://picsum.photos/100",
                        isCurrent = false,
                        storyList = listOf(
                            Story(
                                id = 0,
                                state = StoryState.NOT_SHOWN,
                                link = "https://picsum.photos/500"
                            ),
                            Story(
                                id = 1,
                                state = StoryState.NOT_SHOWN,
                                link = "https://picsum.photos/500"
                            ),
                            Story(
                                id = 2,
                                state = StoryState.NOT_SHOWN,
                                link = "https://picsum.photos/500"
                            )
                        )
                    )
                ),
                isPause = false
            )
        )
    }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var lastDownTime by remember { mutableStateOf(0L) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event: MotionEvent ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastDownTime = System.currentTimeMillis()
                        storyDetails = storyDetails.copy(isPause = true)
                    }
                    MotionEvent.ACTION_UP -> {
                        val upTime = System.currentTimeMillis()
                        if (upTime - lastDownTime <= MAX_TIME_FOR_CLICK) {
                            val isNext = event.x > boxSize.width / 2
                            storyDetails = updateStoryDetails(storyDetails, isNext)
                        }
                        storyDetails = storyDetails.copy(isPause = false)
                    }
                }
                true
            }
            .onSizeChanged { size -> boxSize = size }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .zIndex(1f)
        ) {
            storyDetails.currentStoryGroup?.storyList?.forEachIndexed { index, story ->
                StoryProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = getStartPadding(index, 4.dp)),
                    isPaused = storyDetails.isPause,
                    storyState = story.state,
                ) {
                    storyDetails = updateStoryDetails(storyDetails, true)
                }
            }
        }
        storyDetails.currentStoryGroup?.currentStory?.let { story ->
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(story.link)
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .memoryCacheKey(story.id.toString())
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = "story",
            )
        }
    }
}

private fun updateStoryDetails(storyDetails: StoryDetails, isNext: Boolean): StoryDetails {
    return storyDetails.copy(
        storyGroupList = storyDetails.storyGroupList.map { storyGroup ->
            if (storyGroup.isCurrent) {
                storyGroup.copy(storyList = updateStoryList(storyGroup.storyList, isNext))
            } else {
                storyGroup
            }
        }
    )
}

private fun updateStoryList(storyList: List<Story>, isNext: Boolean): List<Story> {
    return storyList.mapIndexed { i, story ->
        when (story.state) {
            StoryState.SHOWN -> {
                updateShown(
                    isNext = isNext,
                    story = story,
                    nextStory = storyList.getOrNull(i + 1)
                )
            }
            StoryState.IN_PROGRESS -> {
                updateInProgress(isNext = isNext, story = story)
            }
            StoryState.NOT_SHOWN -> {
                updateNotShown(
                    isNext = isNext,
                    story = story,
                    previousStory = storyList.getOrNull(i - 1)
                )
            }
        }
    }
}

private fun updateShown(isNext: Boolean, story: Story, nextStory: Story?): Story {
    return if (!isNext && (nextStory?.state == StoryState.IN_PROGRESS)) {
        story.copy(state = StoryState.IN_PROGRESS)
    } else {
        story
    }
}

private fun updateInProgress(isNext: Boolean, story: Story): Story {
    val newState = if (isNext) StoryState.SHOWN else StoryState.NOT_SHOWN
    return story.copy(state = newState)
}

private fun updateNotShown(isNext: Boolean, story: Story, previousStory: Story?): Story {
    return if (isNext && (previousStory == null || previousStory.state == StoryState.IN_PROGRESS)) {
        story.copy(state = StoryState.IN_PROGRESS)
    } else {
        story
    }
}

@Composable
@Preview
fun StoryDetailsScreenPreview() {
    StoryDetailsScreen()
}