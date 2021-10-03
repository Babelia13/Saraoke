package com.babelia.saraoke.lyrics

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist
import timber.log.Timber

/**
 * Utils for song lyrics.
 */
object LyricsUtils {

    /**
     * Find the lyrics of a song in a Genius HTML website.
     * Lyrics are in a div with "lyrics" class.
     */
    fun getLyricsFromGeniusUrl(htmlUrl: String): String? {
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