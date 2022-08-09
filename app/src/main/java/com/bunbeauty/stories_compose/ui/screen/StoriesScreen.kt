package com.bunbeauty.stories_compose.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.stories_compose.model.StoryPreview
import com.bunbeauty.stories_compose.ui.component.LoadingStoryItem
import com.bunbeauty.stories_compose.ui.component.SuccessStoryItem
import com.bunbeauty.stories_compose.ui.theme.Stories_ComposeTheme
import com.bunbeauty.stories_compose.ui.theme.getStartPadding
import kotlinx.coroutines.delay

@Composable
fun StoriesScreen() {
    val storiesState = remember { mutableStateOf(listOf<StoryPreview>()) }
    Stories_ComposeTheme {
        Stories(storiesState.value)
    }
    LaunchedEffect(rememberScaffoldState()) {
        storiesState.value = List(10) { StoryPreview.Loading }
        delay(2_000L)
        storiesState.value = List(10) { i ->
            delay(100)
            StoryPreview.Success("story #$i", "https://picsum.photos/100")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Stories(storyList: List<StoryPreview>) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyRow(contentPadding = PaddingValues(12.dp)) {
            itemsIndexed(storyList) { i, story ->
                if (story is StoryPreview.Success) {
                    SuccessStoryItem(
                        modifier = Modifier.padding(start = getStartPadding(i)),
                        story = story
                    )
                } else {
                    LoadingStoryItem(
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                }
            }
        }
    }
}