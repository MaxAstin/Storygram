package com.bunbeauty.stories_compose.ui.screen.stories

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.stories_compose.model.StoryPreview
import com.bunbeauty.stories_compose.navigation.Destinations.STORY_DETAILS_ROTE
import com.bunbeauty.stories_compose.ui.component.LoadingStoryItem
import com.bunbeauty.stories_compose.ui.component.SuccessStoryItem
import com.bunbeauty.stories_compose.ui.theme.Stories_ComposeTheme
import com.bunbeauty.stories_compose.ui.theme.getStartPadding

@Composable
fun StoriesScreen(
    navController: NavController,
    viewModel: StoriesViewModel = hiltViewModel()
) {
    val storiesState: StoriesState by viewModel.storiesState.collectAsState()
    Stories_ComposeTheme {
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
            listOf(
                StoryPreview(
                    groupId = 1,
                    name = "story #1",
                    previewLink = "",
                ),
                StoryPreview(
                    groupId = 2,
                    name = "story #2",
                    previewLink = "",
                ),
                StoryPreview(
                    groupId = 3,
                    name = "story #3",
                    previewLink = "",
                ),
            )
        ),
        navController
    )
}