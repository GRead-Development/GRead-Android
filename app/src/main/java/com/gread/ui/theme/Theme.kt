package com.gread.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006E5E),
    secondary = Color(0xFF4F6358),
    tertiary = Color(0xFF386667),
    background = Color(0xFFFBFDF9),
    surface = Color(0xFFFBFDF9),
    onBackground = Color(0xFF191C1A),
    onSurface = Color(0xFF191C1A)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7FD8BD),
    secondary = Color(0xFFB1CCBC),
    tertiary = Color(0xFFB0CFCE),
    background = Color(0xFF0F1410),
    surface = Color(0xFF191C1A),
    onBackground = Color(0xFFE1E3E0),
    onSurface = Color(0xFFE1E3E0)
)

@Composable
fun GReadTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
