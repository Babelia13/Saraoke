package com.babelia.saraoke.lyrics

import com.babelia.saraoke.network.LyricsAndSongArt
import mini.Resource

/**
 * Interface that lyrics controllers must comply to in order to work.
 */
interface LyricsController {

    /**
     * Start listening [MediaSessionManager] changes.
     */
    fun startListeningMediaSessionManagerChanges()

    /**
     * Stop listening [MediaSessionManager] changes.
     */
    fun stopListeningMediaSessionManagerChanges()

    /**
     * Get the lyrics of a song given a query.
     */
    suspend fun getLyricsBySearchSong(song: Song): Resource<LyricsAndSongArt>
}