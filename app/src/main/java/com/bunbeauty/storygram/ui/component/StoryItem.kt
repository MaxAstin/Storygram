package com.bunbeauty.storygram.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bunbeauty.storygram.model.StoryPreview
import com.bunbeauty.storygram.ui.theme.*

private val storyBrush = Brush.linearGradient(
    listOf(
        DeepPurple500,
        Purple500,
        DeepOrange500,
        Orange500,
        Amber500
    )
)

@Composable
fun SuccessStoryItem(
    modifier: Modifier = Modifier,
    story: StoryPreview
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.size(storyPreviewSize)) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(storyBrush)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Gray300)
            )
            CachedImageWithLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(CircleShape),
                imageLink = story.previewLink,
                cacheKey = story.groupId.toString()
            ) { modifier ->
                Shimmer(modifier)
            }
        }
        Text(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(storyPreviewSize)
                .height(storyPreviewTextHeight),
            text = story.name,
            style = TextStyle(fontSize = 12.sp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingStoryItem(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Shimmer(
            modifier = Modifier
                .size(storyPreviewSize)
                .clip(CircleShape)
        )
        Shimmer(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(storyPreviewSize)
                .height(storyPreviewTextHeight)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Preview
@Composable
private fun SuccessPreview() {
    SuccessStoryItem(
        modifier = Modifier.padding(end = 8.dp),
        story = StoryPreview(
            groupId = 1,
            name = "story #1",
            previewLink = "",
        )
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    LoadingStoryItem(modifier = Modifier.padding(end = 8.dp))
}