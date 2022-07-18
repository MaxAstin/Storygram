package com.bunbeauty.stories_compose.model

sealed interface StoryPreview {
    object Loading : StoryPreview
    data class Success(val name: String, val previewLink: String) : StoryPreview
}