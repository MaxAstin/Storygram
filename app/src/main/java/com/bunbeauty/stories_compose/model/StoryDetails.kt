package com.bunbeauty.stories_compose.model

data class StoryDetails(
    val storyGroupList: List<StoryGroup>,
    val isPause: Boolean,
) {
    val currentStoryGroup: StoryGroup?
        get() = storyGroupList.find { it.isCurrent }
}