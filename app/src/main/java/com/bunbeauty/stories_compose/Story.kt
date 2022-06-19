package com.bunbeauty.stories_compose

sealed interface Story {
    object Loading : Story
    data class Success(val name: String, val previewLink: String) : Story
}