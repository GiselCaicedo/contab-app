package com.contab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contab.app.ui.theme.BgColor
import com.contab.app.ui.theme.Blue100
import com.contab.app.ui.theme.Blue50
import com.contab.app.ui.theme.Blue500
import com.contab.app.ui.theme.Blue600
import com.contab.app.ui.theme.BorderColor
import com.contab.app.ui.theme.Poppins
import com.contab.app.ui.theme.SurfaceColor
import com.contab.app.ui.theme.TextColor
import com.contab.app.ui.theme.TextSecColor

// ---------------------------------------------------------------------------
// Data models
// ---------------------------------------------------------------------------
private data class InsightQuestion(
    val number: Int,
    val text: String,
    val options: List<String>,
    val insight: String
)

private data class TipCard(
    val icon: ImageVector,
    val title: String,
    val description: String
)

private val QUESTIONS = listOf(
    InsightQuestion(
        number = 1,
        text = "¿Cuántas compras impulsivas realizas al mes?",
        options = listOf("Ninguna", "1–2", "3–5", "Más de 5"),
        insight = "El 68% de freelancers gastan 15% más de lo planeado en compras no esenciales."
    ),
    InsightQuestion(
        number = 2,
        text = "¿Con qué frecuencia comes por fuera?",
        options = listOf("Raramente", "1×/semana", "Varias", "Siempre"),
        insight = "Tu gasto en alimentación es el 2.° más alto. Cocinar en casa puede ahorrar hasta un 40%."
    ),
    InsightQuestion(
        number = 3,
        text = "¿Tienes un presupuesto mensual?",
        options = listOf("Sí, lo sigo", "Sí, a veces", "No tengo", "Lo estoy creando"),
        insight = "Definir un presupuesto por categoría reduce el gasto promedio en un 23%."
    ),
    InsightQuestion(
        number = 4,
        text = "¿Qué porcentaje de tus ingresos ahorras?",
        options = listOf("No ahorro", "< 10%", "10–20%", "> 20%"),
        insight = "La regla 50/30/20: 50% necesidades, 30% gustos, 20% ahorro."
    )
)

private val TIPS = listOf(
    TipCard(
        icon = Icons.Outlined.TrendingUp,
        title = "Meta de ahorro",
        description = "Reduce \$50.000/mes en Compras cocinando 2 días más en casa."
    ),
    TipCard(
        icon = Icons.Outlined.Repeat,
        title = "Revisión semanal",
        description = "Los usuarios que revisan gastos cada semana gastan un 18% menos."
    ),
    TipCard(
        icon = Icons.Outlined.Tune,
        title = "Configura alertas",
        description = "Activa una alerta cuando superes \$300.000 en Compras."
    )
)

// ---------------------------------------------------------------------------
// InsightsScreen
// ---------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InsightsScreen() {
    // answers[i] = selected option index, -1 = unanswered
    val answers = remember { mutableStateListOf(-1, -1, -1, -1) }
    val answeredCount = answers.count { it >= 0 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        // ── Top insight card ──────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Blue500)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "MAYOR ÁREA DE GASTO",
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp,
                        fontFamily = Poppins
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Icon container
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingBag,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        // Name + amount
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Compras",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = Poppins
                            )
                            Text(
                                text = "$ 304.000",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.85f),
                                fontFamily = Poppins
                            )
                        }
                        // Percentage badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.18f))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "46%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = Poppins
                            )
                        }
                    }
                }
            }
        }

        // ── Progress bar ──────────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Conoce tus hábitos",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor,
                        fontFamily = Poppins
                    )
                    Text(
                        text = "$answeredCount/4",
                        fontSize = 12.sp,
                        color = TextSecColor,
                        fontFamily = Poppins
                    )
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Blue100)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (answeredCount / 4f).coerceIn(0f, 1f))
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Blue500)
                    )
                }
            }
        }

        // ── Question cards ────────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QUESTIONS.forEachIndexed { idx, question ->
                    val selectedOption = answers[idx]
                    val isAnswered = selectedOption >= 0

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 1.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = Color.Black.copy(alpha = 0.06f),
                                spotColor = Color.Black.copy(alpha = 0.06f)
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceColor)
                            .then(
                                if (isAnswered) Modifier.border(
                                    width = 3.dp,
                                    color = Blue500,
                                    shape = RoundedCornerShape(16.dp)
                                ) else Modifier
                            )
                            .padding(14.dp)
                    ) {
                        // Question number + text
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Blue50),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${question.number}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Blue600,
                                    fontFamily = Poppins
                                )
                            }
                            Text(
                                text = question.text,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextColor,
                                fontFamily = Poppins,
                                modifier = Modifier.weight(1f),
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        // Options – wrap-flow pill buttons
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            question.options.forEachIndexed { optIdx, option ->
                                val isSelected = selectedOption == optIdx
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isSelected) Blue500 else SurfaceColor)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) Blue500 else BorderColor,
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clickable {
                                            answers[idx] = if (isSelected) -1 else optIdx
                                        }
                                        .padding(horizontal = 12.dp, vertical = 7.dp)
                                ) {
                                    Text(
                                        text = option,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) Color.White else TextSecColor,
                                        fontFamily = Poppins
                                    )
                                }
                            }
                        }

                        // Insight tip shown when answered
                        if (isAnswered) {
                            Spacer(Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Blue50)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lightbulb,
                                    contentDescription = null,
                                    tint = Blue500,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 1.dp)
                                )
                                Text(
                                    text = question.insight,
                                    fontSize = 11.sp,
                                    color = Blue500,
                                    lineHeight = 15.sp,
                                    fontFamily = Poppins
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Recommendations (shown when answeredCount >= 2) ──────────────────
        if (answeredCount >= 2) {
            item {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Recomendaciones",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor,
                        fontFamily = Poppins
                    )
                    Spacer(Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        TIPS.forEach { tip ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(SurfaceColor)
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(11.dp))
                                        .background(Blue50),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = tip.icon,
                                        contentDescription = null,
                                        tint = Blue500,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tip.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextColor,
                                        fontFamily = Poppins
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = tip.description,
                                        fontSize = 11.sp,
                                        color = TextSecColor,
                                        lineHeight = 15.sp,
                                        fontFamily = Poppins
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}
