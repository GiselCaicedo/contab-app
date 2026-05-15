package com.contab.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contab.app.ui.theme.BgColor
import com.contab.app.ui.theme.Blue100
import com.contab.app.ui.theme.Blue300
import com.contab.app.ui.theme.Blue50
import com.contab.app.ui.theme.Blue500
import com.contab.app.ui.theme.Blue700
import com.contab.app.ui.theme.TextSecColor

// ---------------------------------------------------------------------------
// BudgetRingChart
// ---------------------------------------------------------------------------

@Composable
fun BudgetRingChart(
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.12f
        val inset = strokeWidth / 2f
        val arcSize = Size(size.minDimension - strokeWidth, size.minDimension - strokeWidth)
        val topLeft = Offset(
            x = (size.width - arcSize.width) / 2f,
            y = (size.height - arcSize.height) / 2f
        )

        // Track
        drawArc(
            color = Blue50,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Progress
        val sweepAngle = (percentage.coerceIn(0f, 1f)) * 360f
        drawArc(
            color = Blue500,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

// ---------------------------------------------------------------------------
// DonutChart
// ---------------------------------------------------------------------------

data class DonutSlice(
    val label: String,
    val value: Float,
    val color: Color
)

@Composable
fun DonutChart(
    slices: List<DonutSlice>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.14f
        val arcSize = Size(size.minDimension - strokeWidth, size.minDimension - strokeWidth)
        val topLeft = Offset(
            x = (size.width - arcSize.width) / 2f,
            y = (size.height - arcSize.height) / 2f
        )

        // Background track
        drawArc(
            color = Blue50,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )

        val total = slices.sumOf { it.value.toDouble() }.toFloat()
        if (total == 0f) return@Canvas

        var startAngle = -90f
        slices.forEach { slice ->
            val sweep = (slice.value / total) * 360f
            drawArc(
                color = slice.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += sweep
        }
    }
}

// ---------------------------------------------------------------------------
// SpendingHeatmap
// ---------------------------------------------------------------------------

private val heatmapColors = listOf(BgColor, Blue100, Blue300, Blue500, Blue700)
private val dayLabels = listOf("L", "", "M", "", "V", "", "")
private val monthLabels = listOf("Feb" to 0, "Mar" to 4, "Abr" to 8)

@Composable
fun SpendingHeatmap(
    heatData: List<Int>,
    modifier: Modifier = Modifier
) {
    val cellSize: Dp = 14.dp
    val gap: Dp = 3.dp
    val weeks = 12
    val days = 7

    Column(modifier = modifier) {
        // Month labels row
        Row(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()
        ) {
            monthLabels.forEach { (label, weekIndex) ->
                val leadingWidth = (weekIndex * (cellSize.value + gap.value)).dp
                Spacer(modifier = Modifier.width(leadingWidth))
                Text(
                    text = label,
                    fontSize = 8.sp,
                    color = TextSecColor,
                    modifier = Modifier.width(cellSize * 3)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            // Day labels
            Column(
                modifier = Modifier.width(20.dp),
                verticalArrangement = Arrangement.spacedBy(gap)
            ) {
                dayLabels.forEach { label ->
                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .padding(end = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (label.isNotEmpty()) {
                            Text(
                                text = label,
                                fontSize = 8.sp,
                                color = TextSecColor
                            )
                        }
                    }
                }
            }

            // Grid
            val totalCells = weeks * days
            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                for (week in 0 until weeks) {
                    Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                        for (day in 0 until days) {
                            val index = week * days + day
                            val rawLevel = if (index < heatData.size) heatData[index] else 0
                            val level = rawLevel.coerceIn(0, 4)
                            val isToday = index == totalCells - 1
                            val cellColor = heatmapColors[level]

                            Box(
                                modifier = Modifier
                                    .size(cellSize)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(cellColor)
                                    .then(
                                        if (isToday) {
                                            Modifier.border(
                                                width = 1.5.dp,
                                                color = Blue500,
                                                shape = RoundedCornerShape(3.dp)
                                            )
                                        } else Modifier
                                    )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Legend
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Text(
                text = "Menos",
                fontSize = 8.sp,
                color = TextSecColor
            )
            Spacer(modifier = Modifier.width(2.dp))
            heatmapColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Más",
                fontSize = 8.sp,
                color = TextSecColor
            )
        }
    }
}

// ---------------------------------------------------------------------------
// MonthlyBarChart
// ---------------------------------------------------------------------------

@Composable
fun MonthlyBarChart(
    values: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    if (values.isEmpty()) return

    val maxValue = values.max()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
        ) {
            values.forEachIndexed { index, value ->
                val isCurrentMonth = index == values.lastIndex
                val fraction = if (maxValue > 0f) value / maxValue else 0f
                val barColor = if (isCurrentMonth) Blue500 else Blue100

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        val barWidth = size.width
                        val barHeight = size.height * fraction.coerceIn(0.04f, 1f)
                        val radius = minOf(barWidth, 6.dp.toPx())

                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(0f, size.height - barHeight),
                            size = Size(barWidth, barHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
        ) {
            labels.forEachIndexed { index, label ->
                val isCurrentMonth = index == labels.lastIndex
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = if (isCurrentMonth) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isCurrentMonth) Blue500 else TextSecColor,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
