package com.babelia.saraoke.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.babelia.saraoke.lyrics.ui.LyricsScreen
import com.babelia.saraoke.lyrics.ui.LyricsViewModel
import mini.kodein.android.compose.viewModel
import org.kodein.di.compose.androidContextDI

@Suppress("UndocumentedPublicFunction")
@Composable
fun MainNavGraph(modifier: Modifier,
                 navController: NavHostController = rememberNavController(),
                 startDestinationScreenType: ScreenType = LyricsScreenType.Lyrics) {

    NavHost(navController, startDestination = startDestinationScreenType.route) {

        composable(route = LyricsScreenType.Lyrics.route) {
            val lyricsViewModel: LyricsViewModel by it.viewModel(androidContextDI())
            LyricsScreen(lyricsViewModel)
        }
    }
}