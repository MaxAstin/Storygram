package com.bunbeauty.stories_compose.model

data class StoryDetails(
    val storyGroupList: List<StoryGroup>,
    val isPause: Boolean,
) {
    val currentStoryGroup: StoryGroup?
        get() = storyGroupList.find { it.isCurrent }
    val isEdgeReached: Boolean
        get() = currentStoryGroup?.let { storyGroup ->
            storyGroup.storyList.none { story -> story.state == StoryState.IN_PROGRESS }
        } ?: true
}