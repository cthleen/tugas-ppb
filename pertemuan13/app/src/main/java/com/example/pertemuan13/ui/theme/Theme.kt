package com.example.pertemuan13.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    secondary = BlueGrey40,
    onSecondary = Color.White,
    secondaryContainer = BlueGrey80,
    onSecondaryContainer = Color(0xFF1A237E),
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal80,
    onTertiaryContainer = Color(0xFF002020),
    error = Red40,
    onError = Color.White,
    background = Color(0xFFF0F4F8),
    onBackground = Color(0xFF1A237E),
    surface = Color.White,
    onSurface = Color(0xFF1A237E)
)

@Composable
fun Pertemuan13Theme(
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}