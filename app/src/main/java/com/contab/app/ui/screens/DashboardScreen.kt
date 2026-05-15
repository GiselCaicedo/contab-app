package com.contab.app.ui.screens

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.MicNone
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.contab.app.data.ALL_CATEGORIES
import com.contab.app.data.Category
import com.contab.app.data.SAMPLE_TRANSACTIONS
import com.contab.app.data.Transaction
import com.contab.app.data.TransactionMethod
import com.contab.app.data.formatCOP
import com.contab.app.data.friendlyDate
import com.contab.app.data.generateHeatData
import com.contab.app.data.getCategoryById
import com.contab.app.ui.theme.BgColor
import com.contab.app.ui.theme.Blue100
import com.contab.app.ui.theme.Blue200
import com.contab.app.ui.theme.Blue300
import com.contab.app.ui.theme.Blue50
import com.contab.app.ui.theme.Blue500
import com.contab.app.ui.theme.Blue600
import com.contab.app.ui.theme.Blue700
import com.contab.app.ui.theme.Poppins
import com.contab.app.ui.theme.SuccessColor
import com.contab.app.ui.theme.SurfaceColor
import com.contab.app.ui.theme.TextColor
import com.contab.app.ui.theme.TextSecColor

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

private fun categoryIcon(id: String): ImageVector = when (id) {
    "food"          -> Icons.Outlined.Fastfood
    "transport"     -> Icons.Outlined.Train
    "services"      -> Icons.Outlined.WbIncandescent
    "health"        -> Icons.Outlined.LocalHospital
    "entertainment" -> Icons.Outlined.Movie
    "shopping"      -> Icons.Outlined.ShoppingBag
    "education"     -> Icons.Outlined.School
    else            -> Icons.Outlined.ShoppingCart
}

private fun methodIcon(method: TransactionMethod): ImageVector = when (method) {
    TransactionMethod.SCAN   -> Icons.Outlined.CameraAlt
    TransactionMethod.VOICE  -> Icons.Outlined.MicNone
    TransactionMethod.MANUAL -> Icons.Outlined.Edit
}

private fun methodLabel(method: TransactionMethod): String = when (method) {
    TransactionMethod.SCAN   -> "Escaneado"
    TransactionMethod.VOICE  -> "Voz"
    TransactionMethod.MANUAL -> "Manual"
}

// ---------------------------------------------------------------------------
// DashboardScreen
// ---------------------------------------------------------------------------
@Composable
fun DashboardScreen(navController: NavController) {
    val totalSpent = SAMPLE_TRANSACTIONS.sumOf { it.amount }   // 658 200
    val budget     = 1_200_000L
    val available  = budget - totalSpent
    val pct        = (totalSpent.toFloat() / budget.toFloat()).coerceIn(0f, 1f)

    // Top 3 categories by spend
    val categoryTotals = SAMPLE_TRANSACTIONS
        .groupBy { it.categoryId }
        .mapValues { (_, txs) -> txs.sumOf { it.amount } }
        .entries
        .sortedByDescending { it.value }
        .take(3)
        .map { getCategoryById(it.key) to it.value }

    // First 4 transactions grouped by date, descending
    val first4 = SAMPLE_TRANSACTIONS.take(4)
    val groupedTransactions = first4
        .groupBy { it.date }
        .entries
        .sortedByDescending { it.key }

    val heatData = generateHeatData()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor),
    ) {
        // ── Greeting ──────────────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier.padding(top = 18.dp, start = 20.dp, end = 20.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "Buenos días,",
                    fontSize = 12.sp,
                    color = TextSecColor,
                    fontFamily = Poppins,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Juan Daniel",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor,
                        fontFamily = Poppins,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "·",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue500,
                        fontFamily = Poppins,
                    )
                }
            }
        }

        // ── Hero Card ─────────────────────────────────────────────────────────
        item {
            HeroCard(
                totalSpent   = totalSpent,
                available    = available,
                pct          = pct,
                onVerDetalle = { navController.navigate("reports") }
            )
        }

        // ── Heatmap ───────────────────────────────────────────────────────────
        item {
            HeatmapCard(heatData = heatData)
        }

        // ── Top Categorías ────────────────────────────────────────────────────
        item {
            TopCategoriasSection(
                categoryTotals = categoryTotals,
                onVerTodas     = { navController.navigate("reports") }
            )
        }

        // ── Movimientos ───────────────────────────────────────────────────────
        item {
            MovimientosSection(
                groupedTransactions = groupedTransactions,
                onVerTodo           = { navController.navigate("history") }
            )
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

// ---------------------------------------------------------------------------
// Hero Card
// ---------------------------------------------------------------------------
@Composable
private fun HeroCard(
    totalSpent: Long,
    available: Long,
    pct: Float,
    onVerDetalle: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
            .shadow(
                elevation     = 6.dp,
                shape         = RoundedCornerShape(24.dp),
                ambientColor  = Blue100,
                spotColor     = Blue200,
            )
            .clip(RoundedCornerShape(24.dp))
            .background(SurfaceColor)
            .border(1.dp, Blue50, RoundedCornerShape(24.dp))
    ) {
        // ── Decorative concentric circles (clipped inside card) ───────────────
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp))
        ) {
            // Circle 1 – largest (280dp, Blue50, 80% alpha)
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 110.dp, y = (-110).dp)
                    .clip(CircleShape)
                    .background(Blue50.copy(alpha = 0.80f))
            )
            // Circle 2 (200dp, Blue100, 35% alpha)
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 70.dp, y = (-70).dp)
                    .clip(CircleShape)
                    .background(Blue100.copy(alpha = 0.35f))
            )
            // Circle 3 (120dp, Blue200, 20% alpha)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-30).dp)
                    .clip(CircleShape)
                    .background(Blue200.copy(alpha = 0.20f))
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {
            // Top row: pill + ring
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                // Left side
                Column {
                    // "Abril 2026" pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Blue50)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Blue500)
                        )
                        Text(
                            text = "Abril 2026",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Blue700,
                            fontFamily = Poppins,
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Has gastado",
                        fontSize = 11.sp,
                        color = TextSecColor,
                        fontFamily = Poppins,
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = formatCOP(658_200L),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor,
                        fontFamily = Poppins,
                        letterSpacing = (-0.5).sp,
                    )

                    Spacer(Modifier.height(6.dp))

                    // "–12% vs marzo" badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SuccessColor.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.TrendingUp,
                            contentDescription = null,
                            tint = SuccessColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "–12% vs marzo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SuccessColor,
                            fontFamily = Poppins,
                        )
                    }
                }

                // Right: budget ring
                BudgetRing(progress = pct, size = 78.dp, stroke = 6.dp)
            }

            Spacer(Modifier.height(18.dp))

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Blue50)
            )

            Spacer(Modifier.height(14.dp))

            // Footer row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Disponible",
                        fontSize = 11.sp,
                        color = TextSecColor,
                        fontFamily = Poppins,
                    )
                    Text(
                        text = formatCOP(available),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor,
                        fontFamily = Poppins,
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Blue500)
                        .clickable { onVerDetalle() }
                        .padding(horizontal = 16.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ver detalle",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontFamily = Poppins,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Budget ring
// ---------------------------------------------------------------------------
@Composable
private fun BudgetRing(progress: Float, size: Dp, stroke: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .drawWithContent {
                drawContent()
                val strokePx = stroke.toPx()
                val inset    = strokePx / 2f
                val arcSize  = androidx.compose.ui.geometry.Size(
                    this.size.width - strokePx,
                    this.size.height - strokePx
                )
                val topLeft  = androidx.compose.ui.geometry.Offset(inset, inset)
                // Track
                drawArc(
                    color      = Blue50,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter  = false,
                    style      = Stroke(width = strokePx, cap = StrokeCap.Round),
                    topLeft    = topLeft,
                    size       = arcSize,
                )
                // Progress arc
                drawArc(
                    brush      = Brush.sweepGradient(listOf(Blue500, Blue300, Blue500)),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter  = false,
                    style      = Stroke(width = strokePx, cap = StrokeCap.Round),
                    topLeft    = topLeft,
                    size       = arcSize,
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Blue700,
                fontFamily = Poppins,
            )
            Text(
                text = "presupuesto",
                fontSize = 8.sp,
                color = TextSecColor,
                fontFamily = Poppins,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Heatmap Card
// ---------------------------------------------------------------------------
@Composable
private fun HeatmapCard(heatData: List<Int>) {
    val weeks     = 12
    val days      = 7
    val cellSize  = 14.dp
    val gap       = 3.dp

    val dayLabels   = listOf("L", "", "M", "", "V", "", "")
    val monthLabels = listOf("Feb" to 0, "Mar" to 4, "Abr" to 8)

    fun heatColor(level: Int): Color = when (level) {
        1    -> Blue100
        2    -> Blue300
        3    -> Blue500
        4    -> Blue700
        else -> BgColor
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp), ambientColor = Blue50, spotColor = Blue100)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceColor)
            .padding(16.dp)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Actividad de gasto",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    fontFamily = Poppins,
                )
                Text(
                    text = "72 días activos",
                    fontSize = 11.sp,
                    color = Blue500,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                )
            }

            Text(
                text = "Últimas 12 semanas",
                fontSize = 10.sp,
                color = TextSecColor,
                fontFamily = Poppins,
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )

            // Month labels row (offset by day-label column width ~20dp)
            Row(modifier = Modifier.padding(start = 20.dp)) {
                repeat(weeks) { col ->
                    val label = monthLabels.firstOrNull { it.second == col }?.first ?: ""
                    Box(
                        modifier = Modifier.width(cellSize + gap),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (label.isNotEmpty()) {
                            Text(
                                text = label,
                                fontSize = 8.sp,
                                color = TextSecColor,
                                fontFamily = Poppins,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // Grid
            Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                // Day labels column
                Column(
                    modifier = Modifier.padding(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(gap)
                ) {
                    dayLabels.forEach { label ->
                        Box(
                            modifier = Modifier
                                .size(cellSize),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (label.isNotEmpty()) {
                                Text(
                                    text = label,
                                    fontSize = 8.sp,
                                    color = TextSecColor,
                                    fontFamily = Poppins,
                                )
                            }
                        }
                    }
                }

                // Cells: column-major (week × day)
                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                    repeat(weeks) { col ->
                        Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                            repeat(days) { row ->
                                val idx     = col * days + row
                                val level   = heatData.getOrElse(idx) { 0 }
                                val isToday = idx == heatData.size - 1
                                Box(
                                    modifier = Modifier
                                        .size(cellSize)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(heatColor(level))
                                        .then(
                                            if (isToday) Modifier.border(
                                                2.dp, Blue500, RoundedCornerShape(3.dp)
                                            ) else Modifier
                                        )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Legend
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("Menos", fontSize = 9.sp, color = TextSecColor, fontFamily = Poppins)
                Spacer(Modifier.width(2.dp))
                listOf(0, 1, 2, 3, 4).forEach { level ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(heatColor(level))
                    )
                }
                Spacer(Modifier.width(2.dp))
                Text("Más", fontSize = 9.sp, color = TextSecColor, fontFamily = Poppins)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Top Categorías
// ---------------------------------------------------------------------------
@Composable
private fun TopCategoriasSection(
    categoryTotals: List<Pair<Category, Long>>,
    onVerTodas: () -> Unit,
) {
    val totalMonth = SAMPLE_TRANSACTIONS.sumOf { it.amount }.toFloat()

    Column(modifier = Modifier.padding(top = 18.dp, start = 16.dp, end = 16.dp)) {
        // Section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Top categorías",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor,
                fontFamily = Poppins,
            )
            Text(
                text = "Ver todas",
                fontSize = 12.sp,
                color = Blue500,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                modifier = Modifier.clickable { onVerTodas() }
            )
        }

        Spacer(Modifier.height(12.dp))

        if (categoryTotals.isNotEmpty()) {
            val big    = categoryTotals[0]
            val small1 = categoryTotals.getOrNull(1)
            val small2 = categoryTotals.getOrNull(2)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Big card – col 1, spans 2 rows height
                Box(
                    modifier = Modifier
                        .weight(1.2f)
                        .height(165.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Blue700)
                        .padding(16.dp)
                ) {
                    // Translucent icon behind content
                    Icon(
                        imageVector = categoryIcon(big.first.id),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.10f),
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 14.dp, y = 14.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Icon container top
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = categoryIcon(big.first.id),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Label + amount + percentage
                        Column {
                            Text(
                                text = big.first.label.uppercase(),
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.70f),
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.8.sp,
                                fontFamily = Poppins,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = formatCOP(big.second),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = Poppins,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "${"%.0f".format(big.second / totalMonth * 100)}% del mes",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.65f),
                                fontFamily = Poppins,
                            )
                        }
                    }
                }

                // Small cards – col 2
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(small1, small2).forEach { pair ->
                        if (pair != null) {
                            SmallCategoryCard(category = pair.first, amount = pair.second)
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallCategoryCard(category: Category, amount: Long) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceColor)
            .border(1.dp, Blue50, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Blue50),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon(category.id),
                    contentDescription = null,
                    tint = Blue600,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = category.label,
                fontSize = 9.sp,
                color = TextSecColor,
                fontFamily = Poppins,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = formatCOP(amount),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor,
                fontFamily = Poppins,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Movimientos section
// ---------------------------------------------------------------------------
@Composable
private fun MovimientosSection(
    groupedTransactions: List<Map.Entry<String, List<Transaction>>>,
    onVerTodo: () -> Unit,
) {
    Column(modifier = Modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp)) {
        // Section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Movimientos",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor,
                fontFamily = Poppins,
            )
            Text(
                text = "Ver todo",
                fontSize = 12.sp,
                color = Blue500,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                modifier = Modifier.clickable { onVerTodo() }
            )
        }

        Spacer(Modifier.height(12.dp))

        groupedTransactions.forEach { (date, txs) ->
            TransactionDateGroup(date = date, transactions = txs)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun TransactionDateGroup(
    date: String,
    transactions: List<Transaction>,
) {
    val isToday    = date == "2026-04-19"
    val groupTotal = transactions.sumOf { it.amount }
    val day        = date.split("-").getOrElse(2) { "?" }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // Date stamp card
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isToday) Blue500 else SurfaceColor)
                .then(
                    if (!isToday) Modifier.border(1.dp, Blue50, RoundedCornerShape(12.dp))
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isToday) Color.White else TextColor,
                fontFamily = Poppins,
            )
        }

        // Content side
        Column(modifier = Modifier.weight(1f)) {
            // Group header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = friendlyDate(date),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    fontFamily = Poppins,
                )
                Text(
                    text = "–${formatCOP(groupTotal)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue700,
                    fontFamily = Poppins,
                )
            }

            Text(
                text = "${transactions.size} movimiento${if (transactions.size != 1) "s" else ""}",
                fontSize = 10.sp,
                color = TextSecColor,
                fontFamily = Poppins,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // Transaction rows card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceColor)
                    .border(1.dp, Blue50, RoundedCornerShape(14.dp))
            ) {
                Column {
                    transactions.forEachIndexed { idx, tx ->
                        DashboardTransactionRow(tx = tx)
                        if (idx < transactions.lastIndex) {
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

@Composable
private fun DashboardTransactionRow(tx: Transaction) {
    val cat = getCategoryById(tx.categoryId)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Blue50),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = categoryIcon(tx.categoryId),
                contentDescription = null,
                tint = Blue600,
                modifier = Modifier.size(17.dp)
            )
        }

        // Text content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tx.concept,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColor,
                fontFamily = Poppins,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(3.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Category chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Blue50)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = cat.label,
                        fontSize = 9.5.sp,
                        color = Blue700,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                    )
                }
                // Method icon + label
                Icon(
                    imageVector = methodIcon(tx.method),
                    contentDescription = null,
                    tint = TextSecColor,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    text = methodLabel(tx.method),
                    fontSize = 9.5.sp,
                    color = TextSecColor,
                    fontFamily = Poppins,
                )
            }
        }

        // Amount
        Text(
            text = "–${formatCOP(tx.amount)}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            fontFamily = Poppins,
        )
    }
}
