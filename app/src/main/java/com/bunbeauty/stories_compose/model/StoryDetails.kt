package com.bunbeauty.stories_compose.model

data class StoryDetails(
    val name: String,
    val storyList: List<Story>,
    val isPause: Boolean
)