@file:Suppress("MagicNumber")

package com.babelia.saraoke.utils.extensions

import android.content.res.Configuration

/**
 * Returns true if this device has a big screen such as tablets, both small or large ones.
 */
fun Configuration.isTablet() = smallestScreenWidthDp >= 600

/**
 * Returns true if this device is a large tablet (tablet of 10" onwards).
 */
fun Configuration.isLargeTablet() = smallestScreenWidthDp >= 720

/**
 * Returns true if this device is a small tablet (tablet of 7" onwards until 10").
 */
fun Configuration.isSmallTablet() = isTablet() && !isLargeTablet()

/**
 * Returns true if this device is a television.
 */
fun Configuration.isTv() = uiMode and Configuration.UI_MODE_TYPE_MASK == Configuration.UI_MODE_TYPE_TELEVISION