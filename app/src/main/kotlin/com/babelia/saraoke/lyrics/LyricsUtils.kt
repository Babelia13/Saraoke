package com.babelia.saraoke.lyrics

import com.babelia.saraoke.utils.extensions.cleanBlankLines
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.safety.Safelist
import org.jsoup.select.Elements
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
        var lyrics: String? = null
        val doc: Document = Jsoup.connect(htmlUrl).userAgent("Chrome").get()
        // First, try with lyrics class
        val lyricsElements = doc.getElementsByClass("lyrics")
        if (lyricsElements.isNotEmpty()) {
            lyrics = parseLyricsElements(lyricsElements)
        } else {
            // First, try with Lyrics__Container-sc-1ynbvzw- class
            val lyricsElements = doc.getElementsByClass("Lyrics__Container-sc-1ynbvzw-6")
            if (lyricsElements.isNotEmpty()) {
                lyrics = parseLyricsElements(lyricsElements)
            } else {
                // If lyrics class doesn't contain the lyrics, try with lyrics-root id
                val lyricsElement = doc.getElementById("lyrics-root")
                if (lyricsElement == null) {
                    Timber.w("Lyrics element is empty")
                } else {
                    lyrics = parseLyricsElement(lyricsElement).cleanBlankLines()
                }
            }
        }
        return lyrics
    }

    private fun parseLyricsElements(lyricsElements: Elements): String {
        var lyrics = ""
        lyricsElements.forEach { element ->
            lyrics += parseLyricsElement(element)
        }
        return lyrics
    }

    private fun parseLyricsElement(lyricsElement: Element): String {
        val outputSettings = Document.OutputSettings().prettyPrint(false)
        lyricsElement.select("br").append("\\n")
        lyricsElement.select("p").prepend("\\n")
        val htmlStr = lyricsElement.html()
            .replace("\\\\n".toRegex(), "\n")
            .replace("</br> ", "</br>")
        return Jsoup.clean(htmlStr, "", Safelist(), outputSettings)
            .replace("&amp;", "&")
            .replace("\n\n", "\n")
    }
}