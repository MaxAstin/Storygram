package com.bunbeauty.storygram.ui.screen.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bunbeauty.storygram.model.StoryPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor() : ViewModel() {

    private val mutableStoriesState: MutableStateFlow<StoriesState> =
        MutableStateFlow(StoriesState.Loading)
    val storiesState: StateFlow<StoriesState> = mutableStoriesState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2_000)
            mutableStoriesState.value = StoriesState.Success(
                List(10) { i ->
                    StoryPreview(
                        groupId = i,
                        name = "story #$i",
                        previewLink = "https://picsum.photos/id/${i * 98}/100"
                    )
                }
            )
        }
    }
}