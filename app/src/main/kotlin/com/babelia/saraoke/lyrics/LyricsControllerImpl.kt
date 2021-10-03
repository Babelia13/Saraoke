package com.babelia.saraoke.lyrics

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
import android.media.session.PlaybackState
import androidx.appcompat.app.AppCompatActivity
import com.babelia.saraoke.network.LyricsApi
import com.babelia.saraoke.network.NetworkModule
import com.babelia.saraoke.network.getGeniusLyricsUrl
import com.babelia.saraoke.utils.SongNotFoungException
import kotlinx.coroutines.*
import mini.Dispatcher
import mini.Resource
import timber.log.Timber

typealias PackageName = String

/**
 * Implementation for [LyricsController] using the Genius REST API as backend.
 */
class LyricsControllerImpl(private val context: Context,
                           private val dispatcher: Dispatcher,
                           private val lyricsApi: LyricsApi) : LyricsController {

    companion object {
        private const val SEARCH_LYRICS_TIMEOUT_MILLIS = 10_000L
        private const val RETRY_SEARCH_LYRICS_DELAY_MILLIS = 500L

        private const val MEDIA_LOGS_TAG = "MediaSessionManager"
    }

    private var mediaSessionManager: MediaSessionManager? = null

    // Need to have this as global variable. If not, the callback controller doesn't work
    private var mediaController: MediaController? = null

    /**
     * [MediaController.Callback] used to know when a new song is played.
     */
    private val mediaControllerCallback = object : MediaController.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadata?) {
            metadata?.let { onMediaMetadataChanged(it) }
        }

        override fun onPlaybackStateChanged(state: PlaybackState?) {
            Timber.tag(MEDIA_LOGS_TAG).v("Playback state changed: $state")
        }
    }

    /**
     * [OnActiveSessionsChangedListener] used to know when a new media session is added and start
     * listening media changes on it.
     */
    private val mediaSessionListener = OnActiveSessionsChangedListener { mediaControllers: List<MediaController?>? ->
        if (mediaControllers == null) {
            mediaController?.unregisterCallback(mediaControllerCallback)
            mediaController = null
        } else {
            if (mediaControllers.isEmpty()) return@OnActiveSessionsChangedListener
            // Given the documentation: The controllers will be provided in priority order with the most important
            // controller at index 0
            mediaControllers[0]?.let {
                Timber.tag(MEDIA_LOGS_TAG).d("New active session for ${it.packageName}")
                registerCallbackForMediaController(it)
            }
        }
    }

    override fun startListeningMediaSessionManagerChanges() {
        // TODO Check notification permission and ask for it
        if (NotificationListener.isEnabled(context)) {
            try {
                mediaSessionManager =
                    context.getSystemService(AppCompatActivity.MEDIA_SESSION_SERVICE) as MediaSessionManager
                val className = ComponentName(context, NotificationListener::class.java)
                mediaSessionManager!!.addOnActiveSessionsChangedListener(
                    mediaSessionListener,
                    className
                )

                val mediaControllers = mediaSessionManager!!.getActiveSessions(className)
                if (mediaControllers.isEmpty()) return
                // Given the documentation: The controllers will be provided in priority order with the most important
                // controller at index 0
                mediaControllers[0]?.let {
                    Timber.tag(MEDIA_LOGS_TAG).d("Active session for ${it.packageName}")
                    registerCallbackForMediaController(it)
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting media sessions")
            }
        }
    }

    private fun registerCallbackForMediaController(controller: MediaController) {
        // Need to declare as global variable:
        // https://stackoverflow.com/questions/63834619/mediacontrollercompat-callback-only-works-as-a-field
        mediaController = controller
        mediaController?.let {
            Timber.tag(MEDIA_LOGS_TAG).d("Register callback for ${it.packageName}")
            it.metadata?.let { onMediaMetadataChanged(it) }
            it.registerCallback(mediaControllerCallback)
        }
    }

    private fun onMediaMetadataChanged(metadata: MediaMetadata) {
        val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST)
        val album = metadata.getString(MediaMetadata.METADATA_KEY_ALBUM)
        val track = metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
        Timber.tag(MEDIA_LOGS_TAG).v("Metadata changed: $artist > $album > $track")
        if (!artist.isNullOrEmpty() && !album.isNullOrEmpty() && !track.isNullOrEmpty()) {
            // dispatchBlocking because after receiving this action, GetLyricsOfSongAction is executed
            // and we need to finish the execution of NewSongPlayedOnSpotifyAction and theN dispatch
            // the other one. If not, as both actions act over the same state, it is not set properly
            dispatcher.dispatchBlocking(NewSongPlayedAction(artist = artist, album = album, track = track))
        }
    }

    override fun stopListeningMediaSessionManagerChanges() {
        mediaSessionManager?.removeOnActiveSessionsChangedListener(mediaSessionListener)
        mediaSessionManager = null
    }

    override suspend fun getLyricsBySearchSong(song: Song): Resource<String> {
        return withContext(Dispatchers.IO) {

            var retry = true
            try {
                withTimeout(SEARCH_LYRICS_TIMEOUT_MILLIS) {
                    // Debug purposes only, track the number of retry attempts to the lyrics
                    var retryAttempts = 0
                    val querySearchResult = lyricsApi.getLyricsBySearch("${song.artist} ${song.track}")
                    while (retry) {
                        val geniusLyricsUrl = querySearchResult.getGeniusLyricsUrl(song.artist, song.track)
                            ?: return@withTimeout Resource.failure(SongNotFoungException)
                        val lyrics = LyricsUtils.getLyricsFromGeniusUrl("${NetworkModule.GENIUS_URL}$geniusLyricsUrl")

                        if (lyrics != null) {
                            retry = false
                            return@withTimeout Resource.success(lyrics)
                        } else {
                            retryAttempts++
                            Timber.w("Error retrieving song lyrics, retry attempts: $retryAttempts")
                            delay(RETRY_SEARCH_LYRICS_DELAY_MILLIS)
                        }
                    }
                    Resource.failure()
                }
            } catch (e: TimeoutCancellationException) {
                Timber.e(e, "Connection attempts with songs lyrics provider timed out")
                Resource.failure(e)
            } catch (e: Throwable) {
                Timber.e(e, "Error retrieving song lyrics: ${e.message}")
                Resource.failure(e)
            }
        }
    }
}