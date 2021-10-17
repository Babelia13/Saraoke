package com.babelia.saraoke.utils.extensions

import android.app.Activity
import android.view.WindowManager

/**
 * Keep the [Activity] screen turned on.
 */
fun Activity.keepScreenOn() {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}