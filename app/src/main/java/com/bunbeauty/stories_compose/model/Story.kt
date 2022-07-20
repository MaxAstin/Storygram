package com.bunbeauty.stories_compose.model

data class Story(
    val id: Int,
    val state: StoryState,
    val link: String,
)