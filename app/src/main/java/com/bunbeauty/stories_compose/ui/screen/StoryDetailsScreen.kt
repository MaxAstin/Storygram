package com.bunbeauty.stories_compose.ui.screen

import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bunbeauty.stories_compose.model.Story
import com.bunbeauty.stories_compose.model.StoryDetails
import com.bunbeauty.stories_compose.model.StoryState
import com.bunbeauty.stories_compose.ui.component.StoryProgressIndicator
import com.bunbeauty.stories_compose.ui.theme.getStartPadding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StoryDetailsScreen() {
    val storyDetailsState = remember {
        mutableStateOf(
            StoryDetails(
                name = "story #$0",
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
                ),
                isPause = false
            )
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event: MotionEvent ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        storyDetailsState.value = storyDetailsState.value.copy(isPause = true)
                    }
                    MotionEvent.ACTION_UP -> {
                        storyDetailsState.value = storyDetailsState.value.copy(isPause = false)
                    }
                }
                true
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .zIndex(1f)
        ) {
            storyDetailsState.value.storyList.forEachIndexed { index, story ->
                StoryProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = getStartPadding(index, 4.dp)),
                    isPaused = storyDetailsState.value.isPause,
                    storyState = story.state,
                ) {
                    storyDetailsState.value = storyDetailsState.value.copy(
                        storyList = nextStory(storyDetailsState.value.storyList)
                    )
                }
            }
        }
        storyDetailsState.value.storyList.find { story ->
            story.state == StoryState.IN_PROGRESS
        }?.let { story ->
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
    return newStoryList.toList()
}

@Composable
@Preview
fun StoryDetailsScreenPreview() {
    StoryDetailsScreen()
}