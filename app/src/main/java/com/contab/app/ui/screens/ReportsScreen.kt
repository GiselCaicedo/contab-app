package com.contab.app.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.WbIncandescent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contab.app.data.ALL_CATEGORIES
import com.contab.app.data.Category
import com.contab.app.data.SAMPLE_TRANSACTIONS
import com.contab.app.data.formatCOP
import com.contab.app.ui.theme.BgColor
import com.contab.app.ui.theme.Blue100
import com.contab.app.ui.theme.Blue200
import com.contab.app.ui.theme.Blue300
import com.contab.app.ui.theme.Blue400
import com.contab.app.ui.theme.Blue50
import com.contab.app.ui.theme.Blue500
import com.contab.app.ui.theme.Blue600
import com.contab.app.ui.theme.Blue700
import com.contab.app.ui.theme.SurfaceColor
import com.contab.app.ui.theme.TextColor
import com.contab.app.ui.theme.TextSecColor

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------
private fun catIcon(id: String): ImageVector = when (id) {
    "food"          -> Icons.Outlined.Fastfood
    "transport"     -> Icons.Outlined.Train
    "services"      -> Icons.Outlined.WbIncandescent
    "health"        -> Icons.Outlined.LocalHospital
    "entertainment" -> Icons.Outlined.Movie
    "shopping"      -> Icons.Outlined.ShoppingBag
    "education"     -> Icons.Outlined.School
    else            -> Icons.Outlined.ShoppingCart
}

private val DONUT_COLORS = listOf(Blue700, Blue500, Blue400, Blue300, Blue200, Blue100)

// ---------------------------------------------------------------------------
// ReportsScreen
// ---------------------------------------------------------------------------
@Composable
fun ReportsScreen() {
    val spendByCategory = remember {
        SAMPLE_TRANSACTIONS
            .groupBy { it.categoryId }
            .mapValues { (_, txs) -> txs.sumOf { it.amount } }
    }
    val totalSpent = remember { spendByCategory.values.sum() }

    val categoriesWithSpend = remember {
        ALL_CATEGORIES
            .mapNotNull { cat ->
                val amt = spendByCategory[cat.id] ?: return@mapNotNull null
                if (amt > 0L) cat to amt else null
            }
            .sortedByDescending { it.second }
    }

    val periods = listOf("semana", "mes", "trimestre", "año")
    var selectedPeriod by remember { mutableStateOf("mes") }

    val trendValues = listOf(380_000f, 520_000f, 290_000f, 610_000f, 445_000f, totalSpent.toFloat())
    val trendLabels = listOf("Nov", "Dic", "Ene", "Feb", "Mar", "Abr")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        // ── Period selector ──────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceColor)
                    .border(1.dp, Blue50, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    periods.forEach { period ->
                        val isActive = period == selectedPeriod
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .then(
                                    if (isActive) Modifier
                                        .shadow(
                                            elevation = 3.dp,
                                            shape = RoundedCornerShape(9.dp),
                                            ambientColor = Blue500.copy(alpha = 0.20f),
                                            spotColor = Blue500.copy(alpha = 0.20f)
                                        )
                                        .clip(RoundedCornerShape(9.dp))
                                        .background(Blue500)
                                    else Modifier
                                        .clip(RoundedCornerShape(9.dp))
                                        .background(Color.Transparent)
                                )
                                .clickable { selectedPeriod = period }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = period,
                                fontSize = 12.sp,
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isActive) Color.White else TextSecColor,
                            )
                        }
                    }
                }
            }
        }

        // ── Donut chart + legend ─────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .padding(top = 18.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp), ambientColor = Blue50, spotColor = Blue100)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceColor)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Donut canvas with center label
                    Box(
                        modifier = Modifier.size(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(150.dp)) {
                            val strokeW = size.minDimension * 0.14f
                            val arcSz = Size(size.minDimension - strokeW, size.minDimension - strokeW)
                            val tl = Offset(
                                (size.width - arcSz.width) / 2f,
                                (size.height - arcSz.height) / 2f
                            )
                            // Track
                            drawArc(
                                color = Blue50,
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = tl,
                                size = arcSz,
                                style = Stroke(width = strokeW, cap = StrokeCap.Butt)
                            )
                            if (totalSpent > 0L) {
                                var startAngle = -90f
                                categoriesWithSpend.forEachIndexed { idx, (_, amt) ->
                                    val sliceColor = DONUT_COLORS.getOrElse(idx) { Blue100 }
                                    val sweep = (amt.toFloat() / totalSpent.toFloat()) * 360f
                                    drawArc(
                                        color = sliceColor,
                                        startAngle = startAngle,
                                        sweepAngle = sweep,
                                        useCenter = false,
                                        topLeft = tl,
                                        size = arcSz,
                                        style = Stroke(width = strokeW, cap = StrokeCap.Butt)
                                    )
                                    startAngle += sweep
                                }
                            }
                        }
                        // Center label
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Total",
                                fontSize = 9.sp,
                                color = TextSecColor,
                            )
                            Text(
                                text = formatCOP(totalSpent),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextColor,
                            )
                            Text(
                                text = "Abril 2026",
                                fontSize = 9.sp,
                                color = TextSecColor,
                            )
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    // Legend – top 4 categories
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        categoriesWithSpend.take(4).forEachIndexed { idx, (cat, amt) ->
                            val sliceColor = DONUT_COLORS.getOrElse(idx) { Blue100 }
                            val pct = if (totalSpent > 0) (amt * 100f / totalSpent).toInt() else 0
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(sliceColor)
                                )
                                Text(
                                    text = cat.label,
                                    fontSize = 10.5.sp,
                                    color = TextColor,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )
                                Text(
                                    text = "$pct%",
                                    fontSize = 10.sp,
                                    color = TextSecColor,
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Insight chip ──────────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Blue50)
                    .border(1.dp, Blue100, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Blue500),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TrendingUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Column {
                    Text(
                        text = "Gastas 12% menos que en marzo",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF07266E), // Blue800
                    )
                    Text(
                        text = "$ 60.500 de ahorro este mes",
                        fontSize = 9.5.sp,
                        color = Blue700,
                    )
                }
            }
        }

        // ── Monthly trend bar chart ──────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .padding(top = 14.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(14.dp), ambientColor = Blue50, spotColor = Blue100)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceColor)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tendencia mensual",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextColor,
                    )
                    Text(
                        text = "Últimos 6 meses",
                        fontSize = 10.sp,
                        color = TextSecColor,
                    )
                }

                Spacer(Modifier.height(12.dp))

                val maxVal = trendValues.max()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    trendValues.forEachIndexed { idx, value ->
                        val isCurrent = idx == trendValues.lastIndex
                        val fraction = if (maxVal > 0f) value / maxVal else 0f
                        val barColor = if (isCurrent) Blue500 else Blue100

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            ) {
                                val barH = size.height * fraction.coerceIn(0.04f, 1f)
                                val radius = minOf(size.width / 2f, 6.dp.toPx())
                                drawRoundRect(
                                    color = barColor,
                                    topLeft = Offset(0f, size.height - barH),
                                    size = Size(size.width, barH),
                                    cornerRadius = CornerRadius(radius, radius)
                                )
                                // Flatten bottom corners
                                if (barH > radius) {
                                    drawRect(
                                        color = barColor,
                                        topLeft = Offset(0f, size.height - barH + radius),
                                        size = Size(size.width, barH - radius)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    trendLabels.forEachIndexed { idx, label ->
                        val isCurrent = idx == trendLabels.lastIndex
                        Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCurrent) Blue700 else TextSecColor,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        // ── Desglose list ─────────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .padding(top = 6.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = "Desglose",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(14.dp), ambientColor = Blue50, spotColor = Blue100)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceColor)
                ) {
                    Column {
                        categoriesWithSpend.forEachIndexed { idx, (cat, amt) ->
                            val sliceColor = DONUT_COLORS.getOrElse(idx) { Blue100 }
                            val pct = if (totalSpent > 0) (amt.toFloat() / totalSpent.toFloat()) else 0f
                            val isLast = idx == categoriesWithSpend.lastIndex

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 11.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Icon container with color dot badge
                                    Box(modifier = Modifier.size(34.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(RoundedCornerShape(11.dp))
                                                .background(Blue50),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = catIcon(cat.id),
                                                contentDescription = null,
                                                tint = Blue600,
                                                modifier = Modifier.size(17.dp)
                                            )
                                        }
                                        // Dot badge bottom-right with white border
                                        Box(
                                            modifier = Modifier
                                                .size(9.dp)
                                                .align(Alignment.BottomEnd)
                                                .clip(CircleShape)
                                                .background(Color.White)
                                                .padding(1.5.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape)
                                                    .background(sliceColor)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = cat.label,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextColor,
                                            )
                                            Text(
                                                text = formatCOP(amt),
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextColor,
                                            )
                                        }
                                        Spacer(Modifier.height(5.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(4.dp)
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(Blue50)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(pct.coerceIn(0f, 1f))
                                                        .height(4.dp)
                                                        .clip(RoundedCornerShape(2.dp))
                                                        .background(sliceColor)
                                                )
                                            }
                                            Text(
                                                text = "${(pct * 100).toInt()}%",
                                                fontSize = 9.5.sp,
                                                color = TextSecColor,
                                            )
                                        }
                                    }
                                }

                                if (!isLast) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(Blue100)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(32.dp)) }
    }
}
