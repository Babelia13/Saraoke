package com.babelia.saraoke.lyrics

import com.babelia.saraoke.network.LyricsApi
import com.babelia.saraoke.network.NetworkModule
import com.babelia.saraoke.network.getGeniusLyricsUrl
import com.babelia.saraoke.utils.SongNotFoungException
import kotlinx.coroutines.*
import mini.Resource
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist
import timber.log.Timber

/**
 * Interface that lyrics controllers must comply to in order to work.
 */
interface LyricsController {

    /**
     * Get the lyrics of a song given a query.
     */
    suspend fun getLyricsBySearchSong(song: Song): Resource<String>
}

/**
 * Implementation for [LyricsController] using the Genius REST API as backend.
 */
class LyricsControllerImpl(private val lyricsApi: LyricsApi) : LyricsController {

    companion object {
        private const val SEARCH_LYRICS_TIMEOUT_MILLIS = 10_000L
        private const val RETRY_SEARCH_LYRICS_DELAY_MILLIS = 500L
    }

    override suspend fun getLyricsBySearchSong(song: Song): Resource<String> {
        return withContext(Dispatchers.IO) {

            var retry = true

            try {
                withTimeout(SEARCH_LYRICS_TIMEOUT_MILLIS) {
                    // Debug purposes only, track the number of retry attempts to the lyrics
                    var retryAttempts = 0
                    while (retry) {
                        val querySearchResult = lyricsApi.getLyricsBySearch("${song.artist} ${song.track}")
                        val geniusLyricsUrl = querySearchResult.getGeniusLyricsUrl(song.artist, song.track)
                            ?: return@withTimeout Resource.failure(SongNotFoungException)
                        val lyrics = getLyricsFromUrl("${NetworkModule.GENIUS_URL}$geniusLyricsUrl")

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

    private fun getLyricsFromUrl(htmlUrl: String): String? {
        val doc: Document = Jsoup.connect(htmlUrl).get()
        val outputSettings = Document.OutputSettings().prettyPrint(false)
        val lyricsElements = doc.getElementsByClass("lyrics")
        if (lyricsElements.size == 0) {
            Timber.w("Lyrics elements are empty")
        }
        var lyrics: String? = null
        if (lyricsElements.size == 1) {
            lyricsElements.first()?.let { lyricsElement ->
                lyricsElement.select("br").append("\\n")
                lyricsElement.select("p").prepend("\\n")
                val htmlStr = lyricsElement.html()
                    .replace("\\\\n".toRegex(), "\n")
                    .replace("</br> ", "</br>")
                lyrics = Jsoup.clean(htmlStr, "", Safelist(), outputSettings)
                    .replace("&amp;", "&")
            }
        }
        return lyrics
    }

}