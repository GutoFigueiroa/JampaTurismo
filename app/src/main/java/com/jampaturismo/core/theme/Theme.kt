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
    val safe: Color = TurquoiseSafe,      // Turquesa #2EC4B6
    val warning: Color = AmberWarning,    // Âmbar #FFB703
    val danger: Color = CoralDanger       // Coral #E63946
)

val LocalTideColors = staticCompositionLocalOf { TideSemanticColors() }

private val DarkColorScheme = darkColorScheme(
    primary = CyanAqua,
    onPrimary = NavyBlue,
    primaryContainer = NavyBlueSecondary,
    onPrimaryContainer = SoftCyan,
    secondary = SoftCyan,
    onSecondary = NavyBlue,
    background = DarkBackground,
    onBackground = TextPrimaryLight,
    surface = DarkSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight
)

private val LightColorScheme = lightColorScheme(
    primary = NavyBlueSecondary,
    onPrimary = LightSurface,
    primaryContainer = Color(0xFFD0E1FD),
    onPrimaryContainer = NavyBlue,
    secondary = CyanAqua,
    onSecondary = LightSurface,
    background = LightBackground,
    onBackground = TextPrimaryDark,
    surface = LightSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = LightSurfaceVariant,
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
