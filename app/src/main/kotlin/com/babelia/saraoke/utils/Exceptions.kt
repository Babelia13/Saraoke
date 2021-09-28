package com.babelia.saraoke.utils

/**
 * Generic exceptions to use when returning [Resource] errors so we can type the different errors.
 */

/** App's domain [Exception]. **/
open class AppException : Exception()

/** App's domain [IllegalStateException]. */
open class IllegalStateAppException(cause: String? = null): IllegalStateException(cause)

/** App's domain [IllegalArgumentException]. */
open class IllegalArgumentAppException(cause: String? = null): IllegalArgumentException(cause)

// Genius lyrics API
/**
 * Exception thrown when a specific song can not be found in the lyrics platform.
 */
object SongNotFoungException: AppException()