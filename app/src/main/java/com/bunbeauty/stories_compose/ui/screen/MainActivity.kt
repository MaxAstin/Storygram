package com.bunbeauty.stories_compose.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.stories_compose.Story
import com.bunbeauty.stories_compose.ui.component.LoadingStoryItem
import com.bunbeauty.stories_compose.ui.component.SuccessStoryItem
import com.bunbeauty.stories_compose.ui.theme.Stories_ComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val storiesState = remember { mutableStateOf(listOf<Story>()) }
            Stories_ComposeTheme {
                Stories(storiesState.value)
            }
            SideEffect {
                lifecycleScope.launch {
                    storiesState.value = List(10) { Story.Loading }
                    delay(2_000L)
                    storiesState.value = List(10) { i ->
                        delay(100)
                        Story.Success("story #$i", "https://picsum.photos/100")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Stories(storyList: List<Story>) {
    fun getPadding(index: Int): Dp = if (index == 0) 0.dp else 8.dp

    CompositionLocalProvider(
        LocalOverScrollConfiguration provides null
    ) {
        LazyRow(contentPadding = PaddingValues(12.dp)) {
            itemsIndexed(storyList) { i, story ->
                if (story is Story.Success) {
                    SuccessStoryItem(
                        modifier = Modifier.padding(start = getPadding(i)),
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