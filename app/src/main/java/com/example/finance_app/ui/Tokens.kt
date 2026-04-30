package com.example.finance_app.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Tokens {
    // Brand accents
    val Accent = Color(0xFF7B61FF)        // primary violet
    val Accent2 = Color(0xFF4DA8FF)       // electric blue
    val Accent3 = Color(0xFFFF5C8A)       // hot pink
    val Mint = Color(0xFF34E0A1)          // gain green
    val Amber = Color(0xFFFFB84D)
    val Coral = Color(0xFFFF6B6B)

    // Dark surfaces
    val Bg = Color(0xFF0A0B14)            // app background
    val BgRaise = Color(0xFF11131F)       // raised section
    val Surface = Color(0xFF161827)       // card
    val SurfaceHi = Color(0xFF1E2133)     // hovered card
    val Stroke = Color(0x14FFFFFF)        // 8% white border
    val StrokeHi = Color(0x22FFFFFF)

    // Text
    val TextMain = Color(0xFFF5F5FA)
    val TextSec = Color(0xFF8B8FA8)
    val TextDim = Color(0xFF5C6075)

    // Status
    val Success = Color(0xFF34E0A1)
    val Danger = Color(0xFFFF5C7A)
    val Warn = Color(0xFFFFB84D)

    // Aliases used by older code
    val C1 = Accent
    val C2 = Accent2
    val C3 = Accent3
    val Border = Stroke
    val BlueLight = Color(0x1A4DA8FF)

    // Gradients
    val GradHero = Brush.linearGradient(
        colors = listOf(Color(0xFF7B61FF), Color(0xFF4DA8FF), Color(0xFF34E0A1)),
    )
    val GradCard = Brush.linearGradient(
        colors = listOf(Color(0xFF8B6BFF), Color(0xFF4DA8FF)),
    )
    val GradPink = Brush.linearGradient(
        colors = listOf(Color(0xFFFF5C8A), Color(0xFF7B61FF)),
    )
    val GradSurface = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A1D2E), Color(0xFF12141F)),
    )
}
