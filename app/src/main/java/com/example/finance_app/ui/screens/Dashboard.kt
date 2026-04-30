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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.TrendingDown
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.finance_app.data.EntryMethod
import com.example.finance_app.data.FinanceData
import com.example.finance_app.data.Transaction
import com.example.finance_app.ui.Screen
import com.example.finance_app.ui.Tokens
import com.example.finance_app.ui.iconForCategory

@Composable
fun DashboardScreen(onNavigate: (Screen) -> Unit) {
    val total = FinanceData.transactions.sumOf { it.amount }
    val recent = FinanceData.transactions.take(5)
    val byCat = FinanceData.categories.map { cat ->
        cat to FinanceData.transactions.filter { it.catId == cat.id }.sumOf { it.amount }
    }.filter { it.second > 0 }.sortedByDescending { it.second }.take(3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Tokens.Bg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        HeroBalanceCard(total)
        Spacer(Modifier.height(20.dp))

        Text(
            "Top categorías",
            color = Tokens.TextMain,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            byCat.forEach { (cat, amount) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Tokens.Surface)
                        .border(1.dp, Tokens.Stroke, RoundedCornerShape(20.dp))
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .background(cat.light),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(iconForCategory(cat.id), contentDescription = null, tint = cat.color, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        cat.label,
                        color = Tokens.TextSec,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        FinanceData.formatCop(amount),
                        color = Tokens.TextMain,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                "Recientes",
                color = Tokens.TextMain,
                style = MaterialTheme.typography.titleLarge,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onNavigate(Screen.HISTORY) }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    "Ver todo",
                    color = Tokens.Accent2,
                    style = MaterialTheme.typography.labelLarge,
                )
                Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Tokens.Accent2, modifier = Modifier.size(16.dp))
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            recent.forEach { TxCard(it) }
        }
    }
}

@Composable
private fun HeroBalanceCard(total: Long) {
    var animate by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animate = true }
    val sweep by animateFloatAsState(
        if (animate) 1f else 0f,
        spring(stiffness = Spring.StiffnessLow),
        label = "sweep",
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
            .shadow(28.dp, RoundedCornerShape(28.dp), spotColor = Tokens.Accent)
            .clip(RoundedCornerShape(28.dp))
            .background(Tokens.GradHero)
            .padding(24.dp),
    ) {
        // soft glow blob
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color.White.copy(alpha = 0.18f), Color.Transparent),
                    center = Offset(size.width * 0.85f, size.height * 0.15f),
                    radius = size.width * 0.55f,
                ),
                radius = size.width * 0.55f,
                center = Offset(size.width * 0.85f, size.height * 0.15f),
            )
        }

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Text(
                        "ABRIL · 2026",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(
                "Gasto del mes",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                FinanceData.formatCop(total),
                color = Color.White,
                style = MaterialTheme.typography.displayMedium,
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.TrendingDown,
                    contentDescription = null,
                    tint = Tokens.Mint,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "12% menos que marzo",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(Modifier.height(20.dp))

            // Mini sparkline
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                val pts = listOf(0.6f, 0.45f, 0.7f, 0.55f, 0.4f, 0.6f, 0.3f, 0.5f, 0.35f, 0.55f, 0.25f, 0.4f)
                val w = size.width
                val h = size.height
                val step = w / (pts.size - 1)
                val path = Path().apply {
                    pts.forEachIndexed { i, v ->
                        val x = i * step
                        val y = h * v
                        if (i == 0) moveTo(x, y) else lineTo(x, y)
                    }
                }
                val visible = Path().apply {
                    addPath(path)
                }
                drawPath(
                    path = visible,
                    brush = Brush.horizontalGradient(
                        listOf(Color.White.copy(alpha = 0.2f), Color.White, Color.White),
                        endX = w * sweep,
                    ),
                    style = Stroke(width = 3f),
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BalanceStat("Esta semana", FinanceData.formatCop(197800), Modifier.weight(1f))
                BalanceStat("Promedio día", FinanceData.formatCop(38500), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun BalanceStat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.14f))
            .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            label,
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            value,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
fun TxCard(tx: Transaction) {
    val cat = FinanceData.categoryById(tx.catId)
    val methodIcon: ImageVector = when (tx.method) {
        EntryMethod.SCAN -> Icons.Outlined.CameraAlt
        EntryMethod.VOICE -> Icons.Outlined.Mic
        EntryMethod.MANUAL -> Icons.Outlined.Edit
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Tokens.Surface)
            .border(1.dp, Tokens.Stroke, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(cat.light),
            contentAlignment = Alignment.Center,
        ) {
            Icon(iconForCategory(cat.id), contentDescription = null, tint = cat.color, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                tx.concept,
                color = Tokens.TextMain,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
            )
            Spacer(Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    cat.label,
                    color = Tokens.TextSec,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.width(8.dp))
                Box(modifier = Modifier.size(3.dp).clip(RoundedCornerShape(50)).background(Tokens.TextDim))
                Spacer(Modifier.width(8.dp))
                Icon(methodIcon, contentDescription = null, tint = Tokens.TextDim, modifier = Modifier.size(12.dp))
            }
        }
        Text(
            "-${FinanceData.formatCop(tx.amount)}",
            color = Tokens.Coral,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
