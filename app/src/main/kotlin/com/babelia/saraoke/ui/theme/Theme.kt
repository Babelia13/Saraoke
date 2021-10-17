package com.babelia.saraoke.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import com.babelia.saraoke.utils.extensions.isLargeTablet
import com.babelia.saraoke.utils.extensions.isSmallTablet
import com.babelia.saraoke.utils.extensions.isTv

private val DarkColorPalette = darkColors(
    primary = Purple,
    primaryVariant = LightPurple,
    secondary = MidGrey,
    secondaryVariant = MidGrey,
    background = DeepPurple,
    surface = DeepPurple,
    onPrimary = DeepPurple,
    onSecondary = DynamicLight,
    onBackground = DynamicLight,
    onSurface = DynamicLight,
    onError = DynamicLight
)

// Uncomment for light theme
//private val LightColorPalette = lightColors(
//    primary = Purple,
//    primaryVariant = LightPurple,
//    secondary = MidGrey,
//    secondaryVariant = MidGrey,
//    background = DynamicLight,
//    surface = DynamicLight,
//    onPrimary = DeepPurple,
//    onSecondary = DeepPurple,
//    onBackground = DeepPurple,
//    onSurface = DeepPurple,
//    onError = DynamicLight
//)

/**
 * Object to get theme values from a [Composable].
 */
object AppTheme {
    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current

    val resValues: ResourceValues
        @Composable
        get() = LocalAppResValues.current
}

val Dimens: Dimensions
    @Composable
    get() = AppTheme.dimens

private val LocalAppDimens = staticCompositionLocalOf { phoneDimensions }

/**
 * Composable to provide the proper dimens depending on the device screen dimensions.
 */
@Composable
fun ProvideDimens(dimensions: Dimensions,
                  content: @Composable () -> Unit) {
    val dimensionSet = remember { dimensions }
    CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
}

val ResValues: ResourceValues
    @Composable
    get() = AppTheme.resValues

private val LocalAppResValues = staticCompositionLocalOf { phoneResourceValues }

/**
 * Composable to provide the proper dimens depending on the device screen dimensions.
 */
@Composable
fun ProvideResValues(resourceValues: ResourceValues,
                     content: @Composable () -> Unit) {
    val resourceValuesSet = remember { resourceValues }
    CompositionLocalProvider(LocalAppResValues provides resourceValuesSet, content = content)
}

/**
 * Theme for Saraoke app. It includes different configurations depending on the system color theme
 * and the device screen dimensions.
 */
@Composable
fun SaraokeTheme(content: @Composable () -> Unit) {
    val colors = DarkColorPalette
    val configuration = LocalConfiguration.current
    val dimensions = when {
        configuration.isLargeTablet() -> largeTabletDimensions
        configuration.isSmallTablet() -> smallTabletDimensions
        configuration.isTv() -> tvDimensions
        else -> phoneDimensions
    }
    val resourceValues = when {
        configuration.isLargeTablet() -> largeTabletResourceValues
        configuration.isSmallTablet() -> smallTabletResourceValues
        configuration.isTv() -> tvResourceValues
        else -> phoneResourceValues
    }

    ProvideDimens(dimensions = dimensions) {
        ProvideResValues(resourceValues = resourceValues) {
            MaterialTheme(
                colors = colors,
                typography = Typography,
                shapes = Shapes,
                content = content
            )
        }
    }
}