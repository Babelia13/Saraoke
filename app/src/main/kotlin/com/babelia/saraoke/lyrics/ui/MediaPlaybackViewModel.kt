package com.babelia.saraoke.lyrics.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babelia.saraoke.BaseViewModel
import com.babelia.saraoke.lyrics.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import mini.Dispatcher
import mini.flow
import mini.selectNotNull
import org.kodein.di.instance

/**
 * [ViewModel] used to start and stop listening media playback changes.
 */
class MediaPlaybackViewModel(app: Application) : BaseViewModel(app) {

    private val dispatcher: Dispatcher by instance()
    private val lyricsStore: LyricsStore by instance()

    private val _initMediaSessionManagerFlow = MutableStateFlow(NotificationListenerViewData())
    val initMediaSessionManagerFlow: StateFlow<NotificationListenerViewData> get() = _initMediaSessionManagerFlow

    /**
     * Start listening [MediaSessionManager] changes.
     */
    fun startListeningMediaPlaybackChanges() {
        viewModelScope.launch {
            dispatcher.dispatch(StartListeningMediaPlaybackChangesAction)
        }

        lyricsStore.flow()
            .selectNotNull { it.needToAskForNotificationPermission }
            .onEach {
                _initMediaSessionManagerFlow.value = NotificationListenerViewData(it)
            }
            .launchIn(viewModelScope)
    }

    /**
     * Stop listening [MediaSessionManager] changes.
     */
    fun stopListeningMediaPlaybackChanges() {
        viewModelScope.launch {
            dispatcher.dispatch(StopListeningMediaPlaybackChangesAction)
        }
    }
}

@Suppress("UndocumentedPublicClass")
data class NotificationListenerViewData(val needToAskForNotificationPermission: Boolean = false)