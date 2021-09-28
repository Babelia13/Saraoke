package com.babelia.saraoke.lyrics.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.babelia.saraoke.R
import com.babelia.saraoke.ui.components.ErrorScreen
import com.babelia.saraoke.ui.components.FullScreenProgressIndicator
import mini.Resource

@Composable
@Suppress("UndocumentedPublicFunction")
fun LyricsScreen(lyricsViewModel: LyricsViewModel) {

    val lyricsResource: Resource<LyricsViewData> =
        lyricsViewModel.lyricsFlow.collectAsState().value
    when {
        lyricsResource.isLoading -> {
            FullScreenProgressIndicator(Modifier)
        }
        lyricsResource.isSuccess -> {
            val lyricsViewData = lyricsResource.get()
            Lyrics(lyricsViewData.lyrics)
        }
        lyricsResource.isFailure -> {
            // TODO Show different message depending on the error
            ErrorScreen(R.string.song_lyrics_error_message)
        }
    }
}

@Composable
@Suppress("UndocumentedPublicFunction")
fun Lyrics(lyrics: String) {
   LazyColumn(
       modifier = Modifier.fillMaxSize()
   ) {
       item {
           Text(
               modifier = Modifier.padding(16.dp),
               text = lyrics
           )
       }
   }
}