package com.bunbeauty.storygram.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun CachedImage(
    modifier: Modifier = Modifier,
    imageLink: String,
    cacheKey: String? = null,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageLink)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(cacheKey)
            .build(),
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        onState = onState
    )
}