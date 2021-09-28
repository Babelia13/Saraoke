package com.babelia.saraoke.lyrics

import com.babelia.saraoke.network.LyricsApi
import com.babelia.saraoke.utils.extensions.toTask
import mini.Dispatcher
import mini.Reducer
import mini.Store
import mini.Task
import mini.kodein.bindStore
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit

@Suppress("UndocumentedPublicClass", "MagicNumber")
data class LyricsState(val songLyricsTask: Task = Task.idle(),
                       val songLyrics: String? = null,
                       val currentSongOnSpotify: Song? = null) {

    override fun toString(): String {
        val songLyricsString =
            if (songLyrics != null && songLyrics.length >= 75) "${songLyrics.substring(0, 75)}..."
            else songLyrics
        return "LyricsState(" +
                "songLyricsTask=$songLyricsTask, " +
                "currentSongOnSpotify=$currentSongOnSpotify, " +
                "songLyrics=$songLyricsString)"
    }
}

/**
 * Information of a song.
 */
data class Song(val artist: String, val album: String, val track: String)

/**
 * Store in charge of handle [LyricsState] during the app's lifecycle.
 */
@Suppress("UndocumentedPublicFunction")
class LyricsStore(private val lyricsController: LyricsController,
                  private val dispatcher: Dispatcher) : Store<LyricsState>() {

    @Reducer
    fun onNewSongPlayedOnSpotify(action: NewSongPlayedOnSpotifyAction) {
        with(action) {
            setState(
                state.copy(
                    currentSongOnSpotify = Song(artist, album, track)
                ))
        }
    }

    @Reducer
    suspend fun getLyricsOfSong(action: GetLyricsOfSongAction) {
        if (state.songLyricsTask.isLoading) return
        setState(state.copy(songLyricsTask = Task.loading()))
        dispatcher.dispatch(OnTaskLoadingAction("songLyricsTask"))

        val lyricsResource = lyricsController.getLyricsBySearchSong(action.song)
        setState(
            state.copy(
                songLyrics = lyricsResource.getOrNull(),
                songLyricsTask = lyricsResource.toTask()
            )
        )
    }

}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
object LyricsModule {
    fun create() = DI.Module("LyricsModule") {
        bindStore { LyricsStore(instance(), instance()) }
        bind<LyricsController>() with singleton {
            LyricsControllerImpl(instance())
        }
        bind<LyricsApi>() with singleton {
            val retrofit: Retrofit = instance()
            retrofit.create(LyricsApi::class.java)
        }
    }
}