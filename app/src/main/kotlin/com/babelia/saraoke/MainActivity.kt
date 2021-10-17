package com.babelia.saraoke

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.babelia.saraoke.lyrics.MusicBroadcastReceiver
import com.babelia.saraoke.lyrics.ui.MediaPlaybackViewModel
import com.babelia.saraoke.navigation.NavigationGraph
import com.babelia.saraoke.ui.theme.SaraokeTheme
import com.babelia.saraoke.utils.extensions.keepScreenOn
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import mini.kodein.android.viewModel

/**
 * Main content of the application.
 */
class MainActivity : BaseActivity() {

    private val musicBroadcastReceiver = MusicBroadcastReceiver()
    private val mediaPlaybackViewModel: MediaPlaybackViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        keepScreenOn()

        registerReceiver(musicBroadcastReceiver, MusicBroadcastReceiver.getIntentFilter())
        mediaPlaybackViewModel.startListeningMediaPlaybackChanges()

        // Turn off the decor fitting system windows, which means we need to through handling insets
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LyricsApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(musicBroadcastReceiver)
        mediaPlaybackViewModel.stopListeningMediaPlaybackChanges()
    }
}

@Suppress("UndocumentedPublicFunction")
@Composable
fun LyricsApp() {
    val navController = rememberNavController()
    SaraokeTheme {
        ProvideWindowInsets {
            // Update the system bars to be translucent
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight

            val backgroundColor = MaterialTheme.colors.background
            val elevatedBackgroundColor = LocalElevationOverlay.current?.apply(
                color = MaterialTheme.colors.background, elevation = BottomNavigationDefaults.Elevation
            )

            SideEffect {
                systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
                systemUiController.setNavigationBarColor(
                    color = elevatedBackgroundColor ?: backgroundColor,
                    darkIcons = false
                )
            }

            Scaffold { innerPaddings ->
                NavigationGraph(
                    modifier = Modifier.padding(innerPaddings),
                    navController = navController
                )
            }
        }
    }
}