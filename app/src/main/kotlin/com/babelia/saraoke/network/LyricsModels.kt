@file:Suppress("UndocumentedPublicClass", "ConstructorParameterNaming")
package com.babelia.saraoke.network

import com.babelia.saraoke.utils.extensions.formatSongTitle
import org.jsoup.Jsoup

/**
 * Result obtained from the Genius API search request.
 */
data class QuerySearchResult(
    val meta: Meta,
    val response: Response)

/**
 * Find the best Genius song URL option inside the search request result.
 */
fun QuerySearchResult.getGeniusLyricsUrl(artist: String, track: String): String? {
    val hits = response.hits.filter {
        // Need this to remove hidden NBSP that cannot be replaced using .replace("&nbsp;", " ")
        val title = Jsoup.parse(it.result.title).text()
        val fullTitle = Jsoup.parse(it.result.full_title).text()
        val trimmedTrack = track.formatSongTitle()
        val trimmedTitle = title.formatSongTitle()
        val isValidHit = fullTitle.contains(artist)
                && trimmedTrack == trimmedTitle || trimmedTitle.contains(trimmedTrack, true)
        isValidHit
    }
    val bestResult = hits.maxByOrNull { it.result.annotation_count }
    return bestResult?.result?.path
}

data class Meta(
    val status: Int
)

data class Response(
    val hits: List<Hit>
)

data class Hit(
    val highlights: List<Any>,
    val index: String,
    val type: String,
    val result: Result
)

data class Result(
    val annotation_count: Int,
    val api_path: String,
    val full_title: String,
    val header_image_thumbnail_url: String,
    val header_image_url: String,
    val id: Int,
    val lyrics_owner_id: Int,
    val lyrics_state: String,
    val path: String,
    val song_art_image_thumbnail_url: String,
    val song_art_image_url: String,
    val stats: Stats,
    val title: String,
    val title_with_featured: String,
    val url: String,
    val primary_artist: PrimaryArtist
)

data class Stats(
    val unreviewed_annotations: Int,
    val hot: Boolean
)

data class PrimaryArtist(
    val api_path: String,
    val header_image_url: String,
    val id: Int,
    val image_url: String,
    val is_meme_verified: Boolean,
    val is_verified: Boolean,
    val name: String,
    val url: String
)