package com.babelia.saraoke.utils.extensions

private const val REMIX_TAG = "Remix"
private const val DELIMITER = "-"

/**
 * Remove " - Remix" or "(Remix)" from the title and symbols between "( )".
 */
fun String.formatSongTitle() =
    this.replace(REMIX_TAG, "", true).trim()
        .removeSuffix(DELIMITER).trim()
        .replace("'", "")
        .replace("’", "")
        .replace("\\(.*\\)".toRegex(), "").trim()