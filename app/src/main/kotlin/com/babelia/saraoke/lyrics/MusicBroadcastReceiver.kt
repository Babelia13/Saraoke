package com.babelia.saraoke.lyrics

import android.content.Context
import android.content.Intent
import com.babelia.saraoke.BaseBroadcastReceiver
import mini.Dispatcher
import org.kodein.di.instance
import timber.log.Timber

/**
 * [android.content.BroadcastReceiver] in charge of listening when a new song is played in Spotify.
 */
class MusicBroadcastReceiver : BaseBroadcastReceiver() {

    private val dispatcher: Dispatcher by instance()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        with(intent) {
            // Include "com.spotify.music.playbackstatechanged" for playing/pause status changes
            if (action == "com.spotify.music.metadatachanged") {
                Timber.d("Spotify metadata changed: $action")
                val artist = getStringExtra("artist")
                val album = getStringExtra("album")
                val track = getStringExtra("track")
                val playing = getBooleanExtra("playing", false)
                Timber.d("New Spotify song detected: $artist > $album > $track > Is playing? $playing")

                if (!artist.isNullOrEmpty() && !album.isNullOrEmpty() && !track.isNullOrEmpty()) {
                    // dispatchBlocking because after receiving this action, GetLyricsOfSongAction is executed
                    // and we need to finish the execution of NewSongPlayedOnSpotifyAction and theN dispatch
                    // the other one. If not, as both actions act over the same state, it is not set properly
                    dispatcher.dispatchBlocking(NewSongPlayedOnSpotifyAction(artist, album, track))
                }
            }
        }
    }

}