package com.android.smshero.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val SmsHeroTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
