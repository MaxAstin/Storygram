package com.bunbeauty.storygram.ui.screen.stories

import com.bunbeauty.storygram.model.StoryPreview

sealed interface StoriesState {

    object Loading: StoriesState
    data class Success(val storyPreviewList: List<StoryPreview>): StoriesState
}