package com.babelia.saraoke.ui.components.commons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import coil.size.Scale

/**
 * Load an image from the given [imageUrl] and crop it if needed.
 */
@Composable
fun ImageFromUrl(imageUrl: String?,
                 modifier: Modifier = Modifier,
                 @DrawableRes placeholderRes: Int? = null,
                 contentDescription: String? = null,
                 colorFilter: ColorFilter? = null,
                 contentScale: ContentScale = ContentScale.Crop,
                 enableCrossfade: Boolean = true) {
    Image(
        modifier = modifier,
        contentScale = contentScale,
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                scale(Scale.FILL)
                crossfade(enableCrossfade)
                placeholderRes?.let { placeholder(it) }
            }
        ),
        contentDescription = contentDescription,
        colorFilter = colorFilter
    )
}