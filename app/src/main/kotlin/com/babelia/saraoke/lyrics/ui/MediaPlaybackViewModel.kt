package com.babelia.saraoke.lyrics.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babelia.saraoke.BaseViewModel
import com.babelia.saraoke.lyrics.*
import kotlinx.coroutines.launch
import mini.Dispatcher
import org.kodein.di.instance

/**
 * [ViewModel] used to start and stop listening media playback changes.
 */
class MediaPlaybackViewModel(app: Application) : BaseViewModel(app) {

    private val dispatcher: Dispatcher by instance()

    /**
     * Start listening [MediaSessionManager] changes.
     */
    fun startListeningMediaPlaybackChanges() {
        viewModelScope.launch {
            dispatcher.dispatch(StartListeningMediaPlaybackChangesAction)
        }
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