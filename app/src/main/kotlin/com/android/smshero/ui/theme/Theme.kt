package com.android.smshero.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SmsHeroDarkColors = darkColorScheme(
    primary = RazerGreenBright,
    onPrimary = BlackBg,
    secondary = RazerGreenDark,
    background = BlackBg,
    surface = Color(0xFF0A0A0A),
    onSurface = RazerGreen,
)

private val SmsHeroLightColors = lightColorScheme(
    primary = RazerGreenDark,
    onPrimary = WhiteBg,
    secondary = Color(0xFF1A1A1A),
    background = WhiteBg,
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
)

@Composable
fun SmsHeroTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) SmsHeroDarkColors else SmsHeroLightColors,
        content = content,
    )
}
