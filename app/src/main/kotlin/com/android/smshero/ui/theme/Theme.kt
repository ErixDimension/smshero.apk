package com.android.smshero.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SmsHeroLightScheme = lightColorScheme(
    primary = SmsBlueLight,
    onPrimary = SmsLightBackground,
    secondary = SmsBlueDark,
    background = SmsLightBackground,
    surface = SmsLightSurface,
    onSurface = SmsLightText,
    surfaceVariant = SmsLightBorder,
    outline = SmsLightBorder,
    error = SmsError,
    onError = SmsLightBackground
)

private val SmsHeroDarkScheme = darkColorScheme(
    primary = SmsBlueDark,
    onPrimary = SmsLightBackground,
    secondary = SmsBlueDark,
    background = SmsDarkBackground,
    surface = SmsDarkSurface,
    onSurface = SmsDarkText,
    surfaceVariant = SmsDarkBorder,
    outline = SmsDarkBorder,
    error = Color(0xFFFF3B30),
    onError = SmsLightBackground
)

@Composable
fun SmsHeroTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) SmsHeroDarkScheme else SmsHeroLightScheme,
        typography = SmsHeroTypography,
        content = content
    )
}
