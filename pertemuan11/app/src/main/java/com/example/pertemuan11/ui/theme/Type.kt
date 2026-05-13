package com.example.pertemuan11.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Plus Jakarta Sans — font seragam untuk seluruh app
// Untuk menggunakan custom font, download dari:
// https://fonts.google.com/specimen/Plus+Jakarta+Sans
// Letakkan file .ttf di res/font/ dan uncomment FontFamily di bawah.
//
// val PlusJakartaSans = FontFamily(
//     Font(resId = R.font.plus_jakarta_sans_regular,  weight = FontWeight.Normal),
//     Font(resId = R.font.plus_jakarta_sans_medium,   weight = FontWeight.Medium),
//     Font(resId = R.font.plus_jakarta_sans_semibold, weight = FontWeight.SemiBold),
//     Font(resId = R.font.plus_jakarta_sans_bold,     weight = FontWeight.Bold),
//     Font(resId = R.font.plus_jakarta_sans_extrabold,weight = FontWeight.ExtraBold),
// )

// Fallback: gunakan font default sampai file font ditambahkan ke res/font/
val PlusJakartaSans = FontFamily.SansSerif

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp, lineHeight = 38.sp, letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold,
        fontSize = 26.sp, lineHeight = 32.sp, letterSpacing = (-0.3).sp
    ),
    headlineLarge = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold,
        fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = (-0.2).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold,
        fontSize = 18.sp, lineHeight = 24.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 22.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 26.sp, letterSpacing = (-0.2).sp
    ),
    titleMedium = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp, lineHeight = 21.sp
    ),
    titleSmall = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp, lineHeight = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.Normal,
        fontSize = 15.sp, lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.Normal,
        fontSize = 13.sp, lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.Normal,
        fontSize = 11.sp, lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaSans, fontWeight = FontWeight.Medium,
        fontSize = 10.sp, lineHeight = 14.sp, letterSpacing = 0.3.sp
    ),
)