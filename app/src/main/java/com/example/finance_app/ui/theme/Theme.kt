package com.example.finance_app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.finance_app.ui.Tokens

private val FinanceDarkScheme = darkColorScheme(
    primary = Tokens.Accent,
    onPrimary = Color.White,
    secondary = Tokens.Accent2,
    onSecondary = Color.White,
    tertiary = Tokens.Accent3,
    background = Tokens.Bg,
    onBackground = Tokens.TextMain,
    surface = Tokens.Surface,
    onSurface = Tokens.TextMain,
    surfaceVariant = Tokens.SurfaceHi,
    onSurfaceVariant = Tokens.TextSec,
    error = Tokens.Danger,
    outline = Tokens.Stroke,
)

@Composable
fun FinanceappTheme(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Tokens.Bg.toArgb()
            window.navigationBarColor = Tokens.Bg.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }
    MaterialTheme(
        colorScheme = FinanceDarkScheme,
        typography = Typography,
        content = content,
    )
}
