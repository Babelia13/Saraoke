@file:Suppress("LongParameterList", "ConstructorParameterNaming", "MagicNumber")

package com.babelia.saraoke.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Class in charge to store the layouts dimensions.
 */
data class Dimensions(
    val generalDimens: GeneralDimensions = GeneralDimensions(),
    val lyricsDimens: LyricsDimensions = LyricsDimensions()
)

/**
 * Class in charge to store the general layouts dimensions.
 */
data class GeneralDimensions(
    val layout_padding: Dp = 16.dp,
    val elevation: Dp = 10.dp,
)
val smallTabletGeneralDimensions = GeneralDimensions(
    layout_padding = 32.dp,
)
val largeTabletGeneralDimensions = smallTabletGeneralDimensions.copy(
    layout_padding = 42.dp,
)

val tvGeneralDimensions = GeneralDimensions(
    layout_padding = 52.dp,
)

/**
 * Class in charge to store the layouts dimensions related to lyrics.
 */
data class LyricsDimensions(
    val top_vertical_fading_height: Dp = 150.dp,
    val bottom_vertical_fading_height: Dp = 150.dp,
    val song_info_padding_vertical: Dp = 22.dp,
    val song_info_padding_horizontal: Dp = 22.dp,
    val song_info_height: Dp = 100.dp,
    val song_info_height_and_padding: Dp = 160.dp,
    val song_info_image_padding_start: Dp = 16.dp,
    val song_info_image_size: Dp = 100.dp,
    val song_info_image_offset_y: Dp = (-16).dp,
    val song_info_track_padding_horizontal: Dp = 16.dp,
    val song_info_track_padding_bottom: Dp = 8.dp,
    val lyrics_padding: Dp = 50.dp,
    val lyrics_font_size: TextUnit = 24.sp,
    val lyrics_line_height: TextUnit = 38.sp,
)
val smallTabletLyricsDimensions = LyricsDimensions()

val largeTabletLyricsDimensions = smallTabletLyricsDimensions.copy()

val tvLyricsDimensions = LyricsDimensions(
    song_info_padding_vertical = 28.dp,
    song_info_padding_horizontal = 42.dp,
    song_info_height_and_padding = 180.dp,
    lyrics_padding = 62.dp,
    top_vertical_fading_height = 125.dp,
    bottom_vertical_fading_height = 250.dp,
)

// Phone dimensions are the default ones
val phoneDimensions = Dimensions()

// Specific values for small tablets
val smallTabletDimensions = Dimensions(
    generalDimens = smallTabletGeneralDimensions,
    lyricsDimens = smallTabletLyricsDimensions,
)

// Specific values for large tablets
val largeTabletDimensions = smallTabletDimensions.copy(
    generalDimens = largeTabletGeneralDimensions,
    lyricsDimens = largeTabletLyricsDimensions,
)

// Specific values for TVs
val tvDimensions = Dimensions(
    generalDimens = tvGeneralDimensions,
    lyricsDimens = tvLyricsDimensions
)