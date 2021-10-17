@file:Suppress("StringLiteralDuplication", "MaxLineLength")

package com.babelia.saraoke.debug.previews

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.babelia.saraoke.lyrics.Song
import com.babelia.saraoke.lyrics.ui.LyricsAndSongInfo
import com.babelia.saraoke.ui.theme.SaraokeTheme

private const val SONG_LYRICS_MOCK =
    "Se han vuelto a encontrar mis ganas y tus tonterías\n" +
    "Se me ha vuelto a hacer mañana mientras tu dormías\n" +
    "Porque la casualidad no suele hacerme compañía\n" +
    "Se han vuelto a encontrar mis ganas y tus noches frías\n" +
    "\n" +
    "Buscándote, se me pasa el tiempo\n" +
    "Pensándote, me cuesta tanto controlar\n" +
    "Mirándote, fallo en el intento\n" +
    "Callándome\n" +
    "\n" +
    "Que quiero que se junte el hambre con las ganas de comer\n" +
    "Que quiero que se rompa el hielo y se derrita por la piel\n" +
    "Que no quiero que dejemos uno por el otro la casa sin barrer\n" +
    "Quiero que me quieras sin querer\n" +
    "\n" +
    "Si yo te contara que estoy a tiro\n" +
    "Que quiero todo más si es contigo\n" +
    "No hacer como si nada\n" +
    "Hacerte aquello que no tiene nombre\n" +
    "Y que a partir de ahí le pongan tu apellido\n" +
    "Que si de algo hay que morir sea por fuego amigo\n" +
    "Como se diga\n" +
    "Porque tu eres vida\n" +
    "Eres de todo menos de mentira, que ironía\n" +
    "Qué diga todo lo que siento hasta formar aludes\n" +
    "Y que ni aun así te des por aludida\n" +
    "\n" +
    "Buscándote, se me pasa el tiempo\n" +
    "Pensándote, me cuesta tanto controlar\n" +
    "Mirándote, fallo en el intento\n" +
    "Callándome\n" +
    "\n" +
    "Que quiero que se junte el hambre con las ganas de comer\n" +
    "Que quiero que se rompa el hielo y se derrita por la piel\n" +
    "Que no quiero que dejemos uno por el otro la casa sin barrer\n" +
    "Quiero que me quieras sin querer\n" +
    "\n" +
    "Que quiero que me quieras sin querer\n" +
    "\n" +
    "Buscándote, ahora que te encuentro\n" +
    "Pensando que, es la hora de actuar\n" +
    "Mirándote, casi sin aliento\n" +
    "Gritándote\n" +
    "\n" +
    "Que quiero que se junte el hambre con las ganas de comer\n" +
    "Que quiero que se rompa el hielo y se derrita por la piel\n" +
    "Que no quiero que dejemos uno por el otro la casa sin barrer\n" +
    "Quiero que me quieras sin querer\n" +
    "\n" +
    "Que quiero que me quieras sin querer\n" +
    "Que quiero que me quieras"

@Preview(name = "Song info - Phone", showBackground = true)
@Composable
private fun PhoneSongInfoPreview() {
    SongInfoPreview()
}

@Preview(name = "Song info - TV", showBackground = true, widthDp = 960, heightDp = 540,
    uiMode = Configuration.UI_MODE_TYPE_TELEVISION)
@Composable
private fun TvSongInfoPreview() {
    SongInfoPreview()
}

@Composable
private fun SongInfoPreview() {
    val song = Song(artist = "Bely Basarte", album = "Desde mi otro cuarto",
        track = "Malditas ganas", durationInMs = 193)
    val songArtUrl = "https://t2.genius.com/unsafe/220x0/https%3A%2F%2Fimages.genius.com%2F80d880a8183fa7aed88e5355c51bbb11.1000x1000x1.jpg"
    SaraokeTheme {
        LyricsAndSongInfo(song, songArtUrl, SONG_LYRICS_MOCK)
    }
}