package com.babelia.saraoke.navigation

@Suppress("UndocumentedPublicClass")
sealed class LyricsScreenType : ScreenType {
    abstract override val route: String

    object Lyrics : LyricsScreenType() {
        override val route = "lyrics"
    }
}