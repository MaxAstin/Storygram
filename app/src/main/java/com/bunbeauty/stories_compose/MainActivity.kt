package com.bunbeauty.stories_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.stories_compose.ui.theme.Stories_ComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val storiesState = remember { mutableStateOf(emptyList<Story>()) }
            Stories_ComposeTheme {
                Stories(storiesState.value)
            }
            SideEffect {
                lifecycleScope.launch {
                    delay(500L)
                    storiesState.value = allLoadingStories
                    delay(3000L)
                    storiesState.value = someLoadingStories
                    delay(1500L)
                    storiesState.value = loadedStories
                }
            }
        }
    }

    private val allLoadingStories = listOf(
        Story.Loading,
        Story.Loading,
        Story.Loading,
        Story.Loading,
        Story.Loading,
        Story.Loading,
    )
    private val someLoadingStories = listOf(
        Story.Success("est", "https://est.ua/f/_articles/blogs/16778.jpg"),
        Story.Success(
            "thumb",
            "https://thumb-p9.xhcdn.com/a/wRe4bqrXqJrbPNfN1gRJSA/000/111/970/109_100.jpg"
        ),
        Story.Loading,
        Story.Loading,
        Story.Loading,
        Story.Loading,
    )
    private val loadedStories = listOf(
        Story.Success("est", "https://est.ua/f/_articles/blogs/16778.jpg"),
        Story.Success(
            "thumb",
            "https://thumb-p9.xhcdn.com/a/wRe4bqrXqJrbPNfN1gRJSA/000/111/970/109_100.jpg"
        ),
        Story.Success(
            "userapi",
            "https://sun9-88.userapi.com/c4281/u73245453/d_e54e4722.jpg"
        ),
        Story.Success(
            "sun9",
            "https://sun9-68.userapi.com/c4805/u73973843/d_fd52f3e2.jpg"
        ),
        Story.Success(
            "cdn1",
            "https://cdn1.flamp.ru/2819a3b34322b06f20574eac0c19b110_100_100.jpg"
        ),
        Story.Success(
            "vkfaces",
            "https://vk.vkfaces.com/263/g9688200/b_da081e25.jpg"
        )
    )
}

@Composable
private fun Stories(storyList: List<Story>) {
    Row(
        modifier = Modifier
            .wrapContentSize(
                align = Alignment.CenterStart,
                unbounded = true
            )
            .padding(16.dp),
    ) {
        storyList.forEach { story ->
            if (story is Story.Success) {
                SuccessStoryItem(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(72.dp),
                    story = story
                )
            } else {
                LoadingStoryItem(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(72.dp)
                )
            }
        }
    }
}