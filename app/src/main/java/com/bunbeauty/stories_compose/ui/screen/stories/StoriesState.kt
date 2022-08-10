package com.bunbeauty.stories_compose.ui.screen.stories

import com.bunbeauty.stories_compose.model.StoryPreview

sealed interface StoriesState {

    object Loading: StoriesState
    data class Success(val storyPreviewList: List<StoryPreview>): StoriesState
}