package com.example.finance_app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Sans = FontFamily.SansSerif

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Black, fontSize = 40.sp, lineHeight = 44.sp, letterSpacing = (-1).sp),
    displayMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 36.sp, letterSpacing = (-0.5).sp),
    headlineLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp, letterSpacing = (-0.3).sp),
    headlineMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp),
    titleLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp),
    titleMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodyMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
    labelLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 16.sp, letterSpacing = 0.2.sp),
    labelMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 0.5.sp),
    labelSmall = TextStyle(fontFamily = Sans, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, lineHeight = 12.sp, letterSpacing = 1.sp),
)
