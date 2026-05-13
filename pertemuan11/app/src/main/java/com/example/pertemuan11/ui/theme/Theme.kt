package com.example.pertemuan11.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val MarketTeal       = Color(0xFF0F7A6B)
val MarketTealLight  = Color(0xFFE6F4F1)
val MarketTealDark   = Color(0xFF0A5A4E)
val MarketAccent     = Color(0xFFFF6B35)
val MarketAccentLight= Color(0xFFFFF0EB)
val MarketBackground = Color(0xFFF7F8FA)
val MarketSurface    = Color(0xFFFFFFFF)
val MarketSurface2   = Color(0xFFF0F2F5)
val MarketTextDark   = Color(0xFF111827)
val MarketTextGrey   = Color(0xFF6B7280)
val MarketTextLight  = Color(0xFF9CA3AF)
val MarketBorder     = Color(0xFFE5E7EB)

private val LightColorScheme = lightColorScheme(
    primary          = MarketTeal,
    onPrimary        = Color.White,
    primaryContainer = MarketTealLight,
    onPrimaryContainer = MarketTealDark,
    secondary        = MarketAccent,
    onSecondary      = Color.White,
    background       = MarketBackground,
    onBackground     = MarketTextDark,
    surface          = MarketSurface,
    onSurface        = MarketTextDark,
    surfaceVariant   = MarketSurface2,
    onSurfaceVariant = MarketTextGrey,
    outline          = MarketBorder,
)

@Composable
fun MarketSiswaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = AppTypography,
        content     = content
    )
}