package com.bunbeauty.storygram.ui.screen.stories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.storygram.model.StoryPreview
import com.bunbeauty.storygram.navigation.Destinations.STORY_DETAILS_ROTE
import com.bunbeauty.storygram.ui.component.LoadingStoryItem
import com.bunbeauty.storygram.ui.component.SuccessStoryItem
import com.bunbeauty.storygram.ui.theme.StorygramTheme
import com.bunbeauty.storygram.ui.theme.getStartPadding

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun StoriesScreen(
    navController: NavController,
    viewModel: StoriesViewModel = hiltViewModel()
) {
    val storiesState: StoriesState by viewModel.storiesState.collectAsStateWithLifecycle()
    StorygramTheme {
        Stories(storiesState, navController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Stories(storiesState: StoriesState, navController: NavController) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        when (storiesState) {
            is StoriesState.Loading -> {
                LoadingStories()
            }
            is StoriesState.Success -> {
                SuccessStories(storiesState.storyPreviewList, navController)
            }
        }
    }
}

@Composable
private fun LoadingStories() {
    Row(
        modifier = Modifier
            .wrapContentWidth(align = Alignment.Start, unbounded = true)
            .padding(12.dp)
    ) {
        repeat(10) {
            LoadingStoryItem(modifier = Modifier.padding(end = 8.dp))
        }
    }
}

@Composable
private fun SuccessStories(storyPreviewList: List<StoryPreview>, navController: NavController) {
    LazyRow(contentPadding = PaddingValues(12.dp)) {
        itemsIndexed(storyPreviewList) { i, story ->
            SuccessStoryItem(
                modifier = Modifier
                    .clickable {
                        navController.navigate("$STORY_DETAILS_ROTE/${story.groupId}")
                    }
                    .padding(start = getStartPadding(i)),
                story = story
            )
        }
    }
}

@Preview
@Composable
private fun LoadingStoriesPreview() {
    val navController = rememberNavController()
    Stories(StoriesState.Loading, navController)
}

@Preview
@Composable
private fun SuccessStoriesPreview() {
    val navController = rememberNavController()
    Stories(
        StoriesState.Success(
            List(10) { i ->
                StoryPreview(
                    groupId = i,
                    name = "story #$i",
                    previewLink = "",
                )
            }
        ),
        navController
    )
}