package com.bunbeauty.storygram.model

data class StoryGroup(
    val groupId: Int,
    val name: String,
    val previewLink: String,
    val isCurrent: Boolean,
    val storyList: List<Story>,
) {
    val currentStory: Story?
        get() = storyList.find { it.state == StoryState.IN_PROGRESS }
}