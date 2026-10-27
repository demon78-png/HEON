package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val HeonDarkColorScheme = darkColorScheme(
    primary = HeonAccentNeon,
    onPrimary = Color(0xFF0A1202),
    primaryContainer = Color(0xFF263D08),
    onPrimaryContainer = Color(0xFFD6FF79),
    secondary = HeonAccentPurple,
    onSecondary = Color(0xFF1E0A3C),
    secondaryContainer = Color(0xFF3B1F70),
    onSecondaryContainer = Color(0xFFE9D8FD),
    tertiary = HeonAccentBlue,
    onTertiary = Color(0xFF03283E),
    tertiaryContainer = Color(0xFF0C4A6E),
    onTertiaryContainer = Color(0xFFBAE6FD),
    background = HeonBackgroundPrimary,
    onBackground = HeonTextPrimary,
    surface = HeonBackgroundSecondary,
    onSurface = HeonTextPrimary,
    surfaceVariant = HeonCardElevated,
    onSurfaceVariant = HeonTextSecondary,
    outline = HeonBorderSubtle,
    outlineVariant = Color(0xFF2E3342),
    error = HeonError,
    onError = Color.White
)

@Composable
fun HeonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Gaming hubs like HEON are designed with a dedicated dark atmospheric aesthetic
    val colorScheme = HeonDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                // Light icons on dark background
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward-compatible alias for template
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    HeonTheme(darkTheme = darkTheme, content = content)
}
