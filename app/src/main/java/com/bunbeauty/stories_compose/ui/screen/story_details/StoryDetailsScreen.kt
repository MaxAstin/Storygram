package com.bunbeauty.stories_compose.ui.screen.story_details

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.stories_compose.ui.component.CachedImage
import com.bunbeauty.stories_compose.ui.theme.White

private const val MAX_TIME_FOR_CLICK = 300L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StoryDetailsScreen(
    navController: NavController,
    viewModel: StoryDetailsViewModel = hiltViewModel()
) {
    val storyDetailsState by viewModel.storyDetailsState.collectAsState()
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var lastDownTime by remember { mutableStateOf(0L) }

    fun onTouchEvent(event: MotionEvent): Boolean {
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
        return true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size -> boxSize = size }
    ) {
        storyDetailsState?.let { storyDetails ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
            ) {
                StoryTopBar(
                    modifier = Modifier.padding(16.dp),
                    storyDetails = storyDetails,
                    finishedListener = viewModel::switchStoryToNext,
                    onCloseClicked = navController::popBackStack
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInteropFilter(onTouchEvent = ::onTouchEvent)
                )
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
private fun StoryDetailsScreenPreview() {
    val navController = rememberNavController()
    StoryDetailsScreen(navController)
}