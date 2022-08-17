package com.bunbeauty.stories_compose.ui.screen.story_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bunbeauty.stories_compose.model.Story
import com.bunbeauty.stories_compose.model.StoryDetails
import com.bunbeauty.stories_compose.model.StoryGroup
import com.bunbeauty.stories_compose.model.StoryState
import com.bunbeauty.stories_compose.navigation.Arguments.GROUP_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableStoryDetailsState: MutableStateFlow<StoryDetails?> = MutableStateFlow(null)
    val storyDetailsState: StateFlow<StoryDetails?> = mutableStoryDetailsState.asStateFlow()

    init {
        val groupId = savedStateHandle[GROUP_ID] ?: 0
        viewModelScope.launch {
            mutableStoryDetailsState.value = StoryDetails(
                storyGroupList = List(10) { i ->
                    StoryGroup(
                        groupId = i,
                        name = "story #$i",
                        previewLink = "https://picsum.photos/id/$i/100",
                        isCurrent = i == groupId,
                        storyList = List(4) { j ->
                            val storyState = when {
                                i < groupId -> {
                                    StoryState.SHOWN
                                }
                                i == groupId -> {
                                    if (j == 0) {
                                        StoryState.IN_PROGRESS
                                    } else {
                                        StoryState.NOT_SHOWN
                                    }
                                }
                                else -> {
                                    StoryState.NOT_SHOWN
                                }
                            }
                            val storyId = i * 100 + j
                            Story(
                                id = storyId,
                                state = storyState,
                                link = "https://picsum.photos/id/$storyId/500"
                            )
                        }
                    )
                },
                isPause = false
            )
        }
    }

    fun pauseStory() {
        mutableStoryDetailsState.value = mutableStoryDetailsState.value?.copy(isPause = true)
    }

    fun resumeStory() {
        mutableStoryDetailsState.value = mutableStoryDetailsState.value?.copy(isPause = false)
    }

    fun switchStoryToNext() {
        updateStory(true)
    }

    fun switchStoryToPrevious() {
        updateStory(false)
    }

    private fun updateStory(isNext: Boolean) {
        mutableStoryDetailsState.value?.let { storyDetails ->
            mutableStoryDetailsState.value = storyDetails.copy(
                storyGroupList = storyDetails.storyGroupList.map { storyGroup ->
                    if (storyGroup.isCurrent) {
                        storyGroup.copy(storyList = updateStoryList(storyGroup.storyList, isNext))
                    } else {
                        storyGroup
                    }
                }
            )
        }
    }

    private fun updateStoryList(storyList: List<Story>, isNext: Boolean): List<Story> {
        return storyList.mapIndexed { i, story ->
            when (story.state) {
                StoryState.SHOWN -> {
                    updateShown(
                        isNext = isNext,
                        story = story,
                        nextStory = storyList.getOrNull(i + 1)
                    )
                }
                StoryState.IN_PROGRESS -> {
                    updateInProgress(isNext = isNext, story = story)
                }
                StoryState.NOT_SHOWN -> {
                    updateNotShown(
                        isNext = isNext,
                        story = story,
                        previousStory = storyList.getOrNull(i - 1)
                    )
                }
            }
        }
    }

    private fun updateShown(isNext: Boolean, story: Story, nextStory: Story?): Story {
        return if (!isNext && (nextStory?.state == StoryState.IN_PROGRESS)) {
            story.copy(state = StoryState.IN_PROGRESS)
        } else {
            story
        }
    }

    private fun updateInProgress(isNext: Boolean, story: Story): Story {
        val newState = if (isNext) StoryState.SHOWN else StoryState.NOT_SHOWN
        return story.copy(state = newState)
    }

    private fun updateNotShown(isNext: Boolean, story: Story, previousStory: Story?): Story {
        return if (isNext && (previousStory == null || previousStory.state == StoryState.IN_PROGRESS)) {
            story.copy(state = StoryState.IN_PROGRESS)
        } else {
            story
        }
    }
}