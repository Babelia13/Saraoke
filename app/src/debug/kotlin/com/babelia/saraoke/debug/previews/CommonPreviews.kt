package com.babelia.saraoke.debug.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.babelia.saraoke.R
import com.babelia.saraoke.lyrics.ui.Lyrics
import com.babelia.saraoke.ui.components.ErrorScreen
import com.babelia.saraoke.ui.theme.AndroidArchitectureExampleTheme

@Suppress("UndocumentedPublicFunction")
@Preview(name = "Full screen error", showBackground = true)
@Composable
private fun ErrorPreview() {
    AndroidArchitectureExampleTheme {
        ErrorScreen(R.string.song_lyrics_error_message)
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview(name = "Character", showBackground = true)
@Composable
private fun CharacterPreview() {
    AndroidArchitectureExampleTheme {
        Lyrics("Lorem ipsum")
    }
}