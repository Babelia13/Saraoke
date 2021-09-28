package com.babelia.saraoke

import android.content.BroadcastReceiver
import org.kodein.di.DIAware

/**
 * Base [BroadcastReceiver], for implementing functionality shared by all broadcast receiver.
 */
abstract class BaseBroadcastReceiver : BroadcastReceiver(), DIAware {
    override val di = app.di
    // Implement here common functionality
}