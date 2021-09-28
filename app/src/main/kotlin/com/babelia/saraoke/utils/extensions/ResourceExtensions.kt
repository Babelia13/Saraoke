package com.babelia.saraoke.utils.extensions

import mini.Resource
import mini.Task
import mini.map

/**
 * Convert this [Resource] in to a [Task].
 */
fun Resource<*>.toTask(): Task = this.map { null }