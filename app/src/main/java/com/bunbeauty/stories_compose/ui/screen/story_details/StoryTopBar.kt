package com.bunbeauty.stories_compose.ui.screen.story_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bunbeauty.stories_compose.R
import com.bunbeauty.stories_compose.model.Story
import com.bunbeauty.stories_compose.model.StoryDetails
import com.bunbeauty.stories_compose.model.StoryGroup
import com.bunbeauty.stories_compose.model.StoryState
import com.bunbeauty.stories_compose.ui.component.CachedImage
import com.bunbeauty.stories_compose.ui.component.StoryProgressIndicator
import com.bunbeauty.stories_compose.ui.theme.TransientBlack
import com.bunbeauty.stories_compose.ui.theme.White
import com.bunbeauty.stories_compose.ui.theme.getStartPadding

@Composable
fun StoryTopBar(
    modifier: Modifier = Modifier,
    storyDetails: StoryDetails,
    finishedListener: (() -> Unit),
    onCloseClicked: (() -> Unit),
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            storyDetails.currentStoryGroup?.storyList?.forEachIndexed { index, story ->
                StoryProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = getStartPadding(index, 4.dp)),
                    isPaused = storyDetails.isPause,
                    storyState = story.state,
                    finishedListener = finishedListener
                )
            }
        }
        storyDetails.currentStoryGroup?.let { storyGroup ->
            Row(modifier = Modifier.padding(top = 8.dp)) {
                CachedImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    imageLink = storyGroup.previewLink,
                    cacheKey = storyGroup.groupId.toString()
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = storyGroup.name,
                    style = TextStyle(color = White)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(TransientBlack)
                        .align(Alignment.CenterVertically)
                        .clickable(onClick = onCloseClicked)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.ic_close),
                        tint = White,
                        contentDescription = "close"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun StoryTopBarPreview() {
    StoryTopBar(
        modifier = Modifier.fillMaxWidth(),
        storyDetails = StoryDetails(
            storyGroupList =
            listOf(
                StoryGroup(
                    groupId = 0,
                    name = "story #0",
                    previewLink = "",
                    isCurrent = true,
                    storyList = listOf(
                        Story(
                            id = 0,
                            groupId = 0,
                            state = StoryState.IN_PROGRESS,
                            link = ""
                        ),
                        Story(
                            id = 1,
                            groupId = 0,
                            state = StoryState.NOT_SHOWN,
                            link = ""
                        ),
                        Story(
                            id = 2,
                            groupId = 0,
                            state = StoryState.NOT_SHOWN,
                            link = ""
                        ),
                    )
                )
            ),
            isPause = false
        ),
        finishedListener = {},
        onCloseClicked = {},
    )
}

