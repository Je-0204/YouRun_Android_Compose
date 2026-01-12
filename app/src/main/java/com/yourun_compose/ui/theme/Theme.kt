package com.yourun_compose.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = MainColor,
    onPrimary = Black,
    primaryContainer = SelectedLightOrange,
    secondary = DatePurple,
    onSecondary = White,
    tertiary = Teal200,
    background = White,
    surface = White,
    onSurface = Black,
    onSurfaceVariant = GrayBorder,
    outline = GrayBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = MainColor,
    onPrimary = Black,
    secondary = Purple200,
    background = Black,
    surface = Gray600,
    onSurfaceVariant = GrayBorder,
    outline = GrayBorder
)

@Composable
fun YouRun_composeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Status Bar color change
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
