package com.bunbeauty.stories_compose.ui.screen.story_details

import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.bunbeauty.stories_compose.ui.component.CachedImage
import com.bunbeauty.stories_compose.ui.component.StoryProgressIndicator
import com.bunbeauty.stories_compose.ui.theme.White
import com.bunbeauty.stories_compose.ui.theme.getStartPadding

private const val MAX_TIME_FOR_CLICK = 300L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StoryDetailsScreen(
    viewModel: StoryDetailsViewModel = hiltViewModel()
) {
    val storyDetailsState by viewModel.storyDetailsState.collectAsState()
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var lastDownTime by remember { mutableStateOf(0L) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event: MotionEvent ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastDownTime = System.currentTimeMillis()
                        viewModel.pauseStory()
                    }
                    MotionEvent.ACTION_UP -> {
                        val upTime = System.currentTimeMillis()
                        if (upTime - lastDownTime <= MAX_TIME_FOR_CLICK) {
                            if (event.x < boxSize.width / 2) {
                                viewModel.switchStoryToPrevious()
                            } else {
                                viewModel.switchStoryToNext()
                            }
                        }
                        viewModel.resumeStory()
                    }
                }
                true
            }
            .onSizeChanged { size -> boxSize = size }
    ) {
        storyDetailsState?.let { storyDetails ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .zIndex(1f)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    storyDetails.currentStoryGroup?.storyList?.forEachIndexed { index, story ->
                        StoryProgressIndicator(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = getStartPadding(index, 4.dp)),
                            isPaused = storyDetails.isPause,
                            storyState = story.state,
                        ) {
                            viewModel.switchStoryToNext()
                        }
                    }
                }
                storyDetails.currentStoryGroup?.let { storyGroup ->
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        CachedImage(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape),
                            imageLink = storyGroup.previewLink,
                            cacheKey = storyGroup.groupId.toString()
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically),
                            text = storyGroup.name,
                            style = TextStyle(color = White)
                        )
                    }
                }
            }
            storyDetails.currentStoryGroup?.currentStory?.let { story ->
                CachedImage(
                    modifier = Modifier.fillMaxSize(),
                    imageLink = story.link
                )
            }
        }
    }
}

@Composable
@Preview
fun StoryDetailsScreenPreview() {
    StoryDetailsScreen()
}