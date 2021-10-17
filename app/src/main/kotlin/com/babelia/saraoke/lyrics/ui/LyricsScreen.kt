package com.babelia.saraoke.lyrics.ui

import android.view.KeyEvent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.babelia.saraoke.R
import com.babelia.saraoke.lyrics.Song
import com.babelia.saraoke.ui.components.ErrorScreen
import com.babelia.saraoke.ui.components.FullScreenProgressIndicator
import com.babelia.saraoke.ui.components.commons.ImageFromUrl
import com.babelia.saraoke.ui.theme.*
import kotlinx.coroutines.launch

private const val MANUAL_SCROLL_VALUE = 100f

@Composable
@Suppress("UndocumentedPublicFunction")
fun LyricsScreen(modifier: Modifier, lyricsViewModel: LyricsViewModel) {

    val lyricsResource = lyricsViewModel.lyricsFlow.collectAsState().value
    val currentSong = lyricsViewModel.currentSongFlow.collectAsState().value

    Scaffold(
        modifier = modifier
    ) {
        when {
            lyricsResource.isLoading -> {
                FullScreenProgressIndicator(Modifier)
            }
            lyricsResource.isSuccess -> {
                val lyricsViewData = lyricsResource.get()
                // Lyrics cannot be null when task is success
                LyricsAndSongInfo(currentSong, lyricsViewData.lyricsAndSongArt.thumbnailUrl,
                    lyricsViewData.lyricsAndSongArt.lyricsUrl!!)
            }
            lyricsResource.isFailure -> {
                ErrorScreen(R.string.song_lyrics_error_message)
            }
        }
    }
}

@Composable
@Suppress("UndocumentedPublicFunction")
fun LyricsAndSongInfo(song: Song?, songArtUrl: String?, lyrics: String) {
    BoxWithConstraints {
        val parentMaxWidth = constraints.maxWidth.toFloat()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(LightPurple, DeepPurple),
                        center = Offset(parentMaxWidth, 0f),
                        radius = 2500f,
                        tileMode = TileMode.Clamp
                    )
                )
        ) {
            Lyrics(
                modifier = Modifier.fillMaxSize(),
                lyrics = lyrics,
                songDurationInMs = song?.durationInMs
            )
            VerticalFadingEdge(
                modifier = Modifier.align(Alignment.TopCenter),
                height = Dimens.lyricsDimens.top_vertical_fading_height,
                colors = listOf(MidDeepPurple, Color.Transparent)
            )
            VerticalFadingEdge(
                modifier = Modifier.align(Alignment.BottomCenter),
                height = Dimens.lyricsDimens.bottom_vertical_fading_height,
                colors = listOf(Color.Transparent, MidDeepPurple)
            )
            song?.let {
                SongInfo(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    song = song,
                    songArtUrl = songArtUrl
                )
            }
        }
    }
}

@Composable
private fun VerticalFadingEdge(modifier: Modifier,
                               height: Dp,
                               colors: List<Color>) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(colors),
                    blendMode = BlendMode.DstOut
                )
            }
    )
}

@Composable
@Suppress("UndocumentedPublicFunction")
fun SongInfo(modifier: Modifier = Modifier, song: Song, songArtUrl: String?) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = Dimens.lyricsDimens.song_info_padding_vertical,
                horizontal = Dimens.lyricsDimens.song_info_padding_horizontal
            )
            .height(Dimens.lyricsDimens.song_info_height)
            .background(DynamicLight, shape = Shapes.large),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageFromUrl(
            modifier = Modifier
                .padding(start = Dimens.lyricsDimens.song_info_image_padding_start)
                .size(size = Dimens.lyricsDimens.song_info_image_size)
                .offset(y = Dimens.lyricsDimens.song_info_image_offset_y)
                .clip(shape = Shapes.large),
            imageUrl = songArtUrl,
            placeholderRes = R.drawable.ic_album_art_placeholder
        )
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = Dimens.lyricsDimens.song_info_track_padding_horizontal)
                    .padding(bottom = Dimens.lyricsDimens.song_info_track_padding_bottom),
                text = song.track,
                style = MaterialTheme.typography.h6,
                color = DeepPurple,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(horizontal = Dimens.lyricsDimens.song_info_track_padding_horizontal),
                text = song.artist,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium,
                color = MidGrey,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Suppress("UndocumentedPublicFunction")
fun Lyrics(modifier: Modifier = Modifier,
           lyrics: String,
           songDurationInMs: Int?) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = FocusRequester()

    val lyricsTextHeight = remember { mutableStateOf(0) }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusTarget()
            .onKeyEvent {
                with(it.nativeKeyEvent) {
                    coroutineScope.launch {
                        when (keyCode) {
                            KeyEvent.KEYCODE_DPAD_UP -> listState.animateScrollBy(-MANUAL_SCROLL_VALUE)
                            KeyEvent.KEYCODE_DPAD_DOWN -> listState.animateScrollBy(MANUAL_SCROLL_VALUE)
                            else -> {
                            }
                        }
                    }
                }
                true
            }
    ) {

        songDurationInMs?.let {
            // Start an animation to the bottom of the lyrics given the song duration. This way you reach the end of
            // the lyrics when the songs ends. This is not 100% accurate for all songs, but it is more or less generic.
            coroutineScope.launch {
                listState.animateScrollBy(
                    value = lyricsTextHeight.value.toFloat(),
                    animationSpec = tween(durationMillis = songDurationInMs, easing = LinearEasing))
                listState.stopScroll()
            }
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.lyricsDimens.lyrics_padding)
                    .padding(
                        top = Dimens.lyricsDimens.lyrics_padding,
                        bottom = Dimens.lyricsDimens.song_info_height_and_padding)
                    .onGloballyPositioned {
                        // Save lyrics text height to start scrolling the view to the bottom automatically
                        if (lyricsTextHeight.value != it.size.height) {
                            lyricsTextHeight.value = it.size.height
                        }
                    },
                text = lyrics,
                fontSize = Dimens.lyricsDimens.lyrics_font_size,
                fontWeight = FontWeight.Light,
                lineHeight = Dimens.lyricsDimens.lyrics_line_height,
                textAlign = TextAlign.Center,
                color = DynamicLight,
            )
        }
    }
}