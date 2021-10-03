package com.babelia.saraoke.lyrics

import mini.Action

/**
 * Triggered for start listening changes in media playback.
 */
@Action
object StartListeningMediaPlaybackChangesAction

/**
 * Triggered for stop listening changes in media playback.
 */
@Action
object StopListeningMediaPlaybackChangesAction

/**
 * Triggered for getting the lyrics of a song given by [song].
 */
@Action
data class GetSongLyricsAction(val song: Song)

/**
 * Triggered when a new song starts to play on Spotify.
 */
@Action
data class NewSongPlayedAction(val artist: String, val album: String, val track: String)

/**
 * Triggered for logging when a task is set to [Task.loading()]] in a suspend [@Reducer] in a [Store].
 * This action won't change anything in the state, it is used only for getting info about when the process has started,
 * as if not, only the final state will be logged in the [LoggerMiddleware].
 */
@Action
data class OnTaskLoadingAction(val task: String)