package com.babelia.saraoke

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.babelia.saraoke.lyrics.MusicBroadcastReceiver
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.babelia.saraoke.navigation.MainNavGraph
import com.babelia.saraoke.ui.components.InsetAwareTopAppBar
import com.babelia.saraoke.ui.theme.AndroidArchitectureExampleTheme
import org.kodein.di.compose.withDI

/**
 * Main content of the application.
 */
class MainActivity : BaseActivity() {

    val musicBroadcastReceiver = MusicBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(musicBroadcastReceiver, IntentFilter().apply {
            addAction("com.spotify.music.metadatachanged")
            addAction("com.spotify.music.playbackstatechanged")
        })

        setContent {
            LyricsApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(musicBroadcastReceiver)
    }
}

@Suppress("UndocumentedPublicFunction")
@Composable
fun LyricsApp() = withDI {
    val navController = rememberNavController()
    AndroidArchitectureExampleTheme {
        // Update the system bars to be translucent
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = MaterialTheme.colors.isLight
        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
        }

        Scaffold(
            topBar = {
                TopBar(navController)
            }
        ) { innerPaddingModifier ->
            MainNavGraph(
                modifier = Modifier.padding(innerPaddingModifier),
                navController = navController
            )
        }
    }
}

// TODO: Probably the to app bar should be in each navigation screen, not one for all the app
@Suppress("UndocumentedPublicFunction")
@Composable
fun TopBar(navController: NavHostController) {
    InsetAwareTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {

        },
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}