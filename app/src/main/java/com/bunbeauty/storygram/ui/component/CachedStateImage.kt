package com.bunbeauty.storygram.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil.compose.AsyncImagePainter

@Composable
fun CachedImageWithLoading(
    modifier: Modifier = Modifier,
    imageLink: String,
    cacheKey: String? = null,
    Loading: @Composable (modifier: Modifier) -> Unit
) {
    var isLoading: Boolean by remember {
        mutableStateOf(true)
    }
    Box {
        CachedImage(
            modifier = modifier,
            imageLink = imageLink,
            cacheKey = cacheKey,
            onState = {
                isLoading = it is AsyncImagePainter.State.Loading
            }
        )
        if (isLoading) {
            Loading(modifier = modifier)
        }
    }
}