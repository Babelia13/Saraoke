package com.babelia.saraoke.lyrics.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babelia.saraoke.BaseViewModel
import com.babelia.saraoke.lyrics.GetSongLyricsAction
import com.babelia.saraoke.lyrics.LyricsState
import com.babelia.saraoke.lyrics.LyricsStore
import com.babelia.saraoke.lyrics.Song
import com.babelia.saraoke.network.LyricsAndSongArt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import mini.Dispatcher
import mini.Resource
import mini.flow
import mini.selectNotNull
import org.kodein.di.instance

/**
 * [ViewModel] used to represent the lyrics of a song.
 */
class LyricsViewModel(app: Application) : BaseViewModel(app) {

    private val dispatcher: Dispatcher by instance()
    private val lyricsStore: LyricsStore by instance()

    private val _lyricsFlow = MutableStateFlow<Resource<LyricsViewData>>(Resource.loading())
    val lyricsFlow: StateFlow<Resource<LyricsViewData>> get() = _lyricsFlow

    private val _currentSongFlow = MutableStateFlow<Song?>(null)
    val currentSongFlow: StateFlow<Song?> get() = _currentSongFlow

    init {

        lyricsStore.flow()
            .selectNotNull { it.songCurrentlyPlaying }
            .onEach {
                _currentSongFlow.value = it
                getLyricsOfSong(it)
            }
            .launchIn(viewModelScope)

        lyricsStore.flow()
            .selectNotNull { LyricsViewData.from(it) }
            .onEach {
                _lyricsFlow.value = it
            }
            .launchIn(viewModelScope)
    }

    private fun getLyricsOfSong(song: Song) {
        viewModelScope.launch {
            dispatcher.dispatch(GetSongLyricsAction(song))
        }
    }
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
data class LyricsViewData(val lyricsAndSongArt: LyricsAndSongArt) {
    companion object {
        fun from(state: LyricsState): Resource<LyricsViewData> = with(state) {
            return when {
                songLyricsTask.isSuccess -> {
                    if (songLyricsAndArtUrl?.lyricsUrl != null && songCurrentlyPlaying != null) {
                        Resource.success(LyricsViewData(songLyricsAndArtUrl))
                    } else {
                        Resource.failure(IllegalArgumentException("Not lyrics found for song: " +
                                "${state.songCurrentlyPlaying}"))
                    }
                }
                songLyricsTask.isFailure ->
                    Resource.failure(songLyricsTask.exceptionOrNull())
                songLyricsTask.isLoading -> Resource.loading()
                else -> Resource.empty()
            }
        }
    }
}