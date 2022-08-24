package com.bunbeauty.storygram.model

data class Story(
    val id: Int,
    val groupId: Int,
    val state: StoryState,
    val link: String,
)