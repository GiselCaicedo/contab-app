package com.contab.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary         = Blue500,
    onPrimary       = SurfaceColor,
    primaryContainer= Blue50,
    secondary       = Blue700,
    background      = BgColor,
    surface         = SurfaceColor,
    onBackground    = TextColor,
    onSurface       = TextColor,
    error           = DangerColor,
)

@Composable
fun ContabTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = ContabTypography,
        content     = content,
    )
}
