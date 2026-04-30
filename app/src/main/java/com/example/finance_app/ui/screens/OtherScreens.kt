package com.example.finance_app.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.finance_app.data.FinanceData
import com.example.finance_app.ui.Tokens
import com.example.finance_app.ui.iconForCategory

@Composable
fun ReportsScreen() {
    var period by remember { mutableStateOf("mes") }
    val byCat = FinanceData.categories.map { cat ->
        cat to FinanceData.transactions.filter { it.catId == cat.id }.sumOf { it.amount }
    }.filter { it.second > 0 }.sortedByDescending { it.second }
    val grandTotal = byCat.sumOf { it.second }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Tokens.Bg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Period selector
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Tokens.Surface)
                .border(1.dp, Tokens.Stroke, RoundedCornerShape(16.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            listOf("semana", "mes", "trimestre", "año").forEach { p ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (period == p) Tokens.Accent else Color.Transparent)
                        .clickable { period = p }
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        p.replaceFirstChar { it.uppercase() },
                        color = if (period == p) Color.White else Tokens.TextSec,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        // Donut + total
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Tokens.Surface)
                .border(1.dp, Tokens.Stroke, RoundedCornerShape(24.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            BoxWithConstraints(contentAlignment = Alignment.Center) {
                AnimatedDonut(byCat = byCat, total = grandTotal)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "TOTAL GASTADO",
                        color = Tokens.TextDim,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        FinanceData.formatCop(grandTotal),
                        color = Tokens.TextMain,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            }
        }

        // Category breakdown bars
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Tokens.Surface)
                .border(1.dp, Tokens.Stroke, RoundedCornerShape(24.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "Por categoría",
                color = Tokens.TextMain,
                style = MaterialTheme.typography.titleLarge,
            )
            byCat.forEach { (cat, amount) ->
                val pct = (amount.toFloat() / grandTotal).coerceIn(0f, 1f)
                val animated by animateFloatAsState(pct, spring(stiffness = Spring.StiffnessMediumLow), label = "bar")
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(9.dp))
                                    .background(cat.light),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(iconForCategory(cat.id), contentDescription = null, tint = cat.color, modifier = Modifier.size(15.dp))
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(
                                cat.label,
                                color = Tokens.TextMain,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Text(
                            FinanceData.formatCop(amount),
                            color = Tokens.TextMain,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Tokens.BgRaise),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animated)
                                .height(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(cat.color.copy(alpha = 0.6f), cat.color)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedDonut(
    byCat: List<Pair<com.example.finance_app.data.Category, Long>>,
    total: Long,
) {
    var animate by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animate = true }
    val progress by animateFloatAsState(
        if (animate) 1f else 0f,
        spring(stiffness = Spring.StiffnessLow),
        label = "donut",
    )
    Canvas(
        modifier = Modifier.size(220.dp),
    ) {
        val stroke = 24f
        val rect = androidx.compose.ui.geometry.Rect(
            offset = androidx.compose.ui.geometry.Offset(stroke, stroke),
            size = androidx.compose.ui.geometry.Size(size.width - stroke * 2, size.height - stroke * 2),
        )
        // background ring
        drawArc(
            color = Tokens.BgRaise,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = rect.topLeft,
            size = rect.size,
            style = Stroke(width = stroke, cap = StrokeCap.Butt),
        )
        var start = -90f
        byCat.forEach { (cat, amount) ->
            val sweep = (amount.toFloat() / total) * 360f * progress
            drawArc(
                color = cat.color,
                startAngle = start,
                sweepAngle = sweep - 1.5f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
            start += sweep
        }
    }
}

@Composable
fun InsightsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Tokens.Bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        InsightCard(
            "Alimentación subió 18%",
            "Este mes gastaste COP 47.000 más que el promedio en restaurantes.",
            Icons.Outlined.TrendingUp,
            Tokens.Warn,
        )
        InsightCard(
            "Ahorraste en Transporte",
            "Has gastado COP 42.000 menos que en marzo. ¡Sigue así!",
            Icons.Outlined.TrendingDown,
            Tokens.Mint,
        )
        InsightCard(
            "Patrón de gasto: viernes",
            "Los viernes son tu día más caro — promedio COP 64.000.",
            Icons.Outlined.Lightbulb,
            Tokens.Accent2,
        )
    }
}

@Composable
private fun InsightCard(title: String, body: String, icon: ImageVector, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = accent.copy(alpha = 0.4f))
            .clip(RoundedCornerShape(22.dp))
            .background(Tokens.Surface)
            .border(1.dp, Tokens.Stroke, RoundedCornerShape(22.dp))
            .padding(18.dp),
        verticalAlignment = Alignment.Top,
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
            Text(
                title,
                color = Tokens.TextMain,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                body,
                color = Tokens.TextSec,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun CategoriesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Tokens.Bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        FinanceData.categories.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { cat ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Tokens.Surface)
                            .border(1.dp, Tokens.Stroke, RoundedCornerShape(22.dp))
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(cat.light),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(iconForCategory(cat.id), contentDescription = null, tint = cat.color, modifier = Modifier.size(26.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            cat.label,
                            color = Tokens.TextMain,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun AddScanScreen(onBack: () -> Unit, onDone: () -> Unit) {
    AddPlaceholder("Escanear recibo", Icons.Outlined.CameraAlt, "Apunta la cámara al recibo", Tokens.Accent2, onBack, onDone)
}

@Composable
fun AddVoiceScreen(onBack: () -> Unit, onDone: () -> Unit) {
    AddPlaceholder("Ingreso por voz", Icons.Outlined.Mic, "Toca el micrófono y dicta tu gasto", Tokens.Accent3, onBack, onDone)
}

@Composable
fun AddManualScreen(onBack: () -> Unit, onDone: () -> Unit) {
    AddPlaceholder("Ingreso manual", Icons.Outlined.Edit, "Completa el formulario", Tokens.Mint, onBack, onDone)
}

@Composable
private fun AddPlaceholder(
    title: String,
    icon: ImageVector,
    hint: String,
    accent: Color,
    onBack: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Tokens.Bg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Tokens.Surface)
                    .border(1.dp, Tokens.Stroke, CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver", tint = Tokens.TextMain, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(14.dp))
            Text(
                title,
                color = Tokens.TextMain,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(accent.copy(alpha = 0.35f), accent.copy(alpha = 0f))
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(28.dp, CircleShape, spotColor = accent)
                        .clip(CircleShape)
                        .background(accent),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                }
            }
            Spacer(Modifier.height(28.dp))
            Text(
                hint,
                color = Tokens.TextSec,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(48.dp))
            Row(
                modifier = Modifier
                    .shadow(20.dp, RoundedCornerShape(28.dp), spotColor = Tokens.Accent)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Tokens.GradHero)
                    .clickable { onDone() }
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    "Guardar gasto",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}
