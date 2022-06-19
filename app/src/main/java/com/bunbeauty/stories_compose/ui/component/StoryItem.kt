package com.bunbeauty.stories_compose.ui.component

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bunbeauty.stories_compose.Story
import com.bunbeauty.stories_compose.ui.theme.*

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
    story: Story.Success
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.size(72.dp)) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(storyBrush)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(Gray300)
            )
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .clip(CircleShape),
                model = story.previewLink,
                contentDescription = "story"
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
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
                .size(72.dp)
                .clip(CircleShape)
        )
        Shimmer(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}