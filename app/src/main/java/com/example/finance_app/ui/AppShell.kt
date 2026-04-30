package com.example.finance_app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class Screen(val label: String) {
    HOME("Inicio"), HISTORY("Historial"), REPORTS("Reportes"),
    INSIGHTS("Insights"), CATEGORIES("Categorías");
}

enum class AddFlow { SCAN, VOICE, MANUAL }

@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Tokens.Bg)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(12.dp, RoundedCornerShape(11.dp), spotColor = Tokens.Accent)
                    .clip(RoundedCornerShape(11.dp))
                    .background(Tokens.GradCard),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Outlined.BarChart, contentDescription = null,
                    tint = Color.White, modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Hola, Juan",
                    color = Tokens.TextSec,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    "Contab",
                    color = Tokens.TextMain,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleIconBtn(Icons.Outlined.Notifications, "Notificaciones")
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .shadow(8.dp, CircleShape, spotColor = Tokens.Accent3)
                    .clip(CircleShape)
                    .background(Tokens.GradPink),
                contentAlignment = Alignment.Center,
            ) {
                Text("JD", color = Color.White, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun CircleIconBtn(icon: ImageVector, desc: String) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Tokens.Surface)
            .border(1.dp, Tokens.Stroke, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = desc, tint = Tokens.TextMain, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun ScreenTitle(screen: Screen, onCategoriesClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            screen.label,
            color = Tokens.TextMain,
            style = MaterialTheme.typography.headlineLarge,
        )
        if (screen == Screen.HISTORY) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Tokens.BlueLight)
                    .clickable { onCategoriesClick() }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    "Categorías",
                    color = Tokens.Accent2,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

private data class NavItem(val screen: Screen, val icon: ImageVector)

private val navItems = listOf(
    NavItem(Screen.HOME, Icons.Outlined.Home),
    NavItem(Screen.HISTORY, Icons.Outlined.ViewList),
    NavItem(Screen.REPORTS, Icons.Outlined.BarChart),
    NavItem(Screen.INSIGHTS, Icons.Outlined.Lightbulb),
)

@Composable
fun BottomNav(
    current: Screen,
    onSelect: (Screen) -> Unit,
    onFab: () -> Unit,
    fabOpen: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Tokens.Bg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp, RoundedCornerShape(28.dp), spotColor = Tokens.Accent)
                .clip(RoundedCornerShape(28.dp))
                .background(Tokens.Surface)
                .border(1.dp, Tokens.Stroke, RoundedCornerShape(28.dp))
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            navItems.subList(0, 2).forEach { NavBtn(it, current == it.screen) { onSelect(it.screen) } }

            FabButton(open = fabOpen, onClick = onFab)

            navItems.subList(2, 4).forEach { NavBtn(it, current == it.screen) { onSelect(it.screen) } }
        }
    }
}

@Composable
private fun FabButton(open: Boolean, onClick: () -> Unit) {
    val rotation by animateFloatAsState(if (open) 45f else 0f, spring(Spring.DampingRatioMediumBouncy), label = "rot")
    val scale by animateFloatAsState(if (open) 1.08f else 1f, spring(Spring.DampingRatioMediumBouncy), label = "scl")
    Box(
        modifier = Modifier
            .scale(scale)
            .size(56.dp)
            .shadow(18.dp, CircleShape, spotColor = Tokens.Accent)
            .clip(CircleShape)
            .background(Tokens.GradHero)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.Outlined.Add,
            contentDescription = "Agregar",
            tint = Color.White,
            modifier = Modifier
                .size(26.dp)
                .scale(scaleX = if (rotation == 0f) 1f else 1f, scaleY = 1f),
        )
    }
}

@Composable
private fun NavBtn(item: NavItem, active: Boolean, onClick: () -> Unit) {
    val tint by animateColorAsState(if (active) Color.White else Tokens.TextSec, label = "tint")
    val pillW by animateDpAsState(if (active) 80.dp else 44.dp, spring(Spring.DampingRatioMediumBouncy), label = "w")
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(pillW)
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) Tokens.Accent.copy(alpha = 0.18f) else Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                item.icon,
                contentDescription = item.screen.label,
                tint = if (active) Tokens.Accent else tint,
                modifier = Modifier.size(20.dp),
            )
            if (active) {
                Spacer(Modifier.width(6.dp))
                Text(
                    item.screen.label,
                    color = Tokens.Accent,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
fun FabMenu(onSelect: (AddFlow) -> Unit, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xCC0A0B14), Color(0xF20A0B14))
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClose() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Nuevo gasto",
                color = Tokens.TextMain,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
            )
            FabOption("Escanear recibo", "Captura con la cámara", Icons.Outlined.CameraAlt, Tokens.Accent2) { onSelect(AddFlow.SCAN) }
            FabOption("Ingreso por voz", "Dicta tu gasto", Icons.Outlined.Mic, Tokens.Accent3) { onSelect(AddFlow.VOICE) }
            FabOption("Ingreso manual", "Llena el formulario", Icons.Outlined.Edit, Tokens.Mint) { onSelect(AddFlow.MANUAL) }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun FabOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = accent.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(20.dp))
            .background(Tokens.Surface)
            .border(1.dp, Tokens.Stroke, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(accent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Tokens.TextMain, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, color = Tokens.TextSec, style = MaterialTheme.typography.bodyMedium)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Tokens.TextDim, modifier = Modifier.size(18.dp))
    }
}
