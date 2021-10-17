package com.babelia.saraoke.utils.extensions

import java.text.Normalizer

private const val REMIX_TAG = "Remix"
private const val DELIMITER = "-"
private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

/**
 * Remove " - Remix" or "(Remix)" from the title and symbols between "( )".
 */
fun String.formatSongTitle() =
    this.replace(REMIX_TAG, "", true).trim()
        .removeSuffix(DELIMITER).trim()
        .replace("'", "")
        .replace("’", "")
        .replace("\\(.*\\)".toRegex(), "").trim()
        .unaccent()

/**
 * Remove accents and diacritics of the given [String].
 */
fun String.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}
