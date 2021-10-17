@file:Suppress("ConstructorParameterNaming")
package com.babelia.saraoke.ui.theme

/**
 * Class in charge to store different resources values such as Int, or Float.
 */
data class ResourceValues(
    // General
    val general_grid_cells: Int = 1,
)

// Phone dimensions are the default ones
val phoneResourceValues = ResourceValues()

// Specific values for small tablets
val smallTabletResourceValues = ResourceValues()

// Specific values for large tablets
val largeTabletResourceValues = ResourceValues()

// Specific values for TVs
val tvResourceValues = ResourceValues()
