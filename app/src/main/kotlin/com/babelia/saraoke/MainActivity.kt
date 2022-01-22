package com.babelia.saraoke

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.babelia.saraoke.lyrics.MusicBroadcastReceiver
import com.babelia.saraoke.lyrics.ui.MediaPlaybackViewModel
import com.babelia.saraoke.navigation.NavigationGraph
import com.babelia.saraoke.ui.EnableNotificationPermissionDialog
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

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        keepScreenOn()

        registerReceiver(musicBroadcastReceiver, MusicBroadcastReceiver.getIntentFilter())

        // Turn off the decor fitting system windows, which means we need to through handling insets
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LyricsApp(mediaPlaybackViewModel,
                openNotificationSettings = {
                    val settingsIntent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    } else {
                        Intent(Settings.ACTION_SETTINGS)
                    }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(settingsIntent)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlaybackViewModel.startListeningMediaPlaybackChanges()
    }

    override fun onStop() {
        super.onStop()
        mediaPlaybackViewModel.stopListeningMediaPlaybackChanges()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(musicBroadcastReceiver)
    }
}

@ExperimentalComposeUiApi
@Suppress("UndocumentedPublicFunction")
@Composable
fun LyricsApp(mediaPlaybackViewModel: MediaPlaybackViewModel, openNotificationSettings: () -> Unit) {
    val navController = rememberNavController()
    SaraokeTheme {
        val notificationListenerViewData = mediaPlaybackViewModel.initMediaSessionManagerFlow.collectAsState().value
        if (notificationListenerViewData.needToAskForNotificationPermission) {
            EnableNotificationPermissionDialog {
                openNotificationSettings()
            }
        } else {
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
}