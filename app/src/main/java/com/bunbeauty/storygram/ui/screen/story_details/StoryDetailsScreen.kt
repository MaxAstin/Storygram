package com.bunbeauty.storygram.ui.screen.story_details

import android.content.Context
import android.view.MotionEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bunbeauty.storygram.model.Story
import com.bunbeauty.storygram.ui.component.CachedImageWithLoading
import com.bunbeauty.storygram.ui.theme.Gray400

private const val MAX_TIME_FOR_CLICK = 300L

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun StoryDetailsScreen(
    navController: NavController,
    viewModel: StoryDetailsViewModel = hiltViewModel()
) {
    val storyDetailsState by viewModel.storyDetailsState.collectAsStateWithLifecycle()
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var lastDownTime by remember { mutableStateOf(0L) }

    if (storyDetailsState?.isEdgeReached == true) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    fun preloadStory(context: Context, story: Story) {
        val request = ImageRequest.Builder(context)
            .data(story.link)
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
        context.imageLoader.enqueue(request)
    }

    val context = LocalContext.current
    LaunchedEffect(storyDetailsState?.currentStoryGroup?.currentStory) {
        viewModel.getStoryListForPreloading()?.forEach { story ->
            preloadStory(context, story)
        }
    }

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
                CachedImageWithLoading(
                    modifier = Modifier.fillMaxSize(),
                    imageLink = story.link
                ) { modifier ->
                    Box(modifier) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Gray400
                        )
                    }
                }
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.resumeStory()
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                viewModel.pauseStory()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
@Preview
private fun StoryDetailsScreenPreview() {
    val navController = rememberNavController()
    StoryDetailsScreen(navController)
}