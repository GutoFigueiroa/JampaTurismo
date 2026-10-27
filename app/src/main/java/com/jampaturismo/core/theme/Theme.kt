package com.jampaturismo.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class TideSemanticColors(
    val safe: Color = TideSafeGreen,
    val warning: Color = TideWarningAmber,
    val danger: Color = TideDangerCoral
)

val LocalTideColors = staticCompositionLocalOf { TideSemanticColors() }

private val DarkColorScheme = darkColorScheme(
    primary = AquaCyan,
    onPrimary = DeepNavy,
    primaryContainer = DeepNavy,
    onPrimaryContainer = SoftCyan,
    secondary = SoftCyan,
    onSecondary = DeepNavy,
    background = DarkBackground,
    onBackground = TextPrimaryLight,
    surface = DarkSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFF1E3A5F),
    onSurfaceVariant = TextSecondaryLight
)

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = OceanBlue,
    secondary = AquaCyan,
    onSecondary = DeepNavy,
    background = SandWhite,
    onBackground = TextPrimaryDark,
    surface = Color.White,
    onSurface = TextPrimaryDark,
    surfaceVariant = SandSurface,
    onSurfaceVariant = TextSecondaryDark
)

val MaterialTheme.tideColors: TideSemanticColors
    @Composable
    get() = LocalTideColors.current

@Composable
fun JampaTurismoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val tideColors = TideSemanticColors()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalTideColors provides tideColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
