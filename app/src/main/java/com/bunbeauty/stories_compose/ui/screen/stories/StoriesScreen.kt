package com.bunbeauty.stories_compose.ui.screen.stories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.stories_compose.model.StoryPreview
import com.bunbeauty.stories_compose.ui.component.LoadingStoryItem
import com.bunbeauty.stories_compose.ui.component.SuccessStoryItem
import com.bunbeauty.stories_compose.ui.theme.Stories_ComposeTheme
import com.bunbeauty.stories_compose.ui.theme.getStartPadding
import kotlinx.coroutines.delay

@Composable
fun StoriesScreen() {
    var storiesState: StoriesState by remember { mutableStateOf(StoriesState.Loading) }
    Stories_ComposeTheme {
        Stories(storiesState)
    }
    LaunchedEffect(rememberScaffoldState()) {
        delay(2_000L)
        storiesState = StoriesState.Success(
            List(10) { i ->
                delay(100)
                StoryPreview(
                    groupId = i,
                    name = "story #$i",
                    previewLink = "https://picsum.photos/100"
                )
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Stories(storiesState: StoriesState) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        when (storiesState) {
            is StoriesState.Loading -> {
                LoadingStories()
            }
            is StoriesState.Success -> {
                SuccessStories(storiesState.storyPreviewList)
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
private fun SuccessStories(storyPreviewList: List<StoryPreview>) {
    LazyRow(contentPadding = PaddingValues(12.dp)) {
        itemsIndexed(storyPreviewList) { i, story ->
            SuccessStoryItem(
                modifier = Modifier.padding(start = getStartPadding(i)),
                story = story
            )
        }
    }
}

@Preview
@Composable
private fun LoadingStoriesPreview() {
    Stories(StoriesState.Loading)
}

@Preview
@Composable
private fun SuccessStoriesPreview() {
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
        )
    )
}