package com.example.pertemuan14.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppDarkColorScheme = darkColorScheme(
    primary = NewsWhite,
    onPrimary = NewsBlack,
    primaryContainer = NewsDarkSurface,
    onPrimaryContainer = NewsWhite,
    secondary = NewsGray,
    onSecondary = NewsWhite,
    secondaryContainer = NewsCard,
    onSecondaryContainer = NewsWhite80,
    tertiary = NewsAccentBlue,
    onTertiary = NewsBlack,
    background = NewsBlack,
    onBackground = NewsWhite,
    surface = NewsDarkSurface,
    onSurface = NewsWhite,
    surfaceVariant = NewsCard,
    onSurfaceVariant = NewsLightGray,
    outline = NewsBorder,
    error = NewsAccentOrange,
    onError = NewsBlack
)

@Composable
fun Pertemuan14Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography = Typography,
        content = content
    )
}