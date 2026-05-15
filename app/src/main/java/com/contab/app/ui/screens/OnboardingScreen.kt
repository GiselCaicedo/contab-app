package com.contab.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.contab.app.ui.theme.Blue300
import com.contab.app.ui.theme.Blue400
import com.contab.app.ui.theme.Blue500
import com.contab.app.ui.theme.Blue700
import kotlinx.coroutines.delay

// ---------- Data model ----------

private data class OnboardingQuestion(
    val id: String,
    val label: String,
    val question: String,
    val options: List<String>
)

private val questions = listOf(
    OnboardingQuestion(
        id = "impulse",
        label = "Pregunta 1",
        question = "¿Cuántas compras impulsivas\nhaces a la semana?",
        options = listOf("Ninguna", "1–2", "3–5", "Más de 5")
    ),
    OnboardingQuestion(
        id = "eating_out",
        label = "Pregunta 2",
        question = "¿Con qué frecuencia\ncomes por fuera?",
        options = listOf("Raramente", "1×/semana", "Varias", "Siempre")
    ),
    OnboardingQuestion(
        id = "budget",
        label = "Pregunta 3",
        question = "¿Tienes un\npresupuesto mensual?",
        options = listOf("Sí, lo sigo", "Sí, a veces", "No tengo", "Lo estoy creando")
    ),
    OnboardingQuestion(
        id = "savings",
        label = "Pregunta 4",
        question = "¿Qué porcentaje de tus\ningresos ahorras?",
        options = listOf("0%", "1–10%", "11–20%", "Más del 20%")
    )
)

// ---------- Screen ----------

@Composable
fun OnboardingScreen(navController: NavController) {
    var step by remember { mutableIntStateOf(0) }
    val answers = remember { mutableStateMapOf<String, String>() }
    var pendingAdvance by remember { mutableStateOf(false) }

    // Auto-advance after selecting the last answer
    LaunchedEffect(pendingAdvance) {
        if (pendingAdvance) {
            delay(180)
            navController.navigate("home") {
                popUpTo("onboarding") { inclusive = true }
            }
        }
    }

    val currentQuestion = questions[step]
    val selectedAnswer = answers[currentQuestion.id]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue500)
    ) {
        // ── Decorative circles ────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(x = 120.dp, y = (-80).dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Blue400.copy(alpha = 0.50f))
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .offset(x = 40.dp, y = 60.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Blue300.copy(alpha = 0.40f))
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-100).dp, y = 80.dp)
                .align(Alignment.BottomStart)
                .clip(CircleShape)
                .background(Blue700.copy(alpha = 0.60f))
        )

        // ── Main layout ───────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section: logo + skip
            TopBar(onSkip = {
                navController.navigate("home") {
                    popUpTo("onboarding") { inclusive = true }
                }
            })

            Spacer(modifier = Modifier.height(28.dp))

            // Progress bar
            ProgressBar(currentStep = step, totalSteps = questions.size)

            Spacer(modifier = Modifier.height(40.dp))

            // Question area
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // "Pregunta N" label
                Text(
                    text = currentQuestion.label.uppercase(),
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Question text
                Text(
                    text = currentQuestion.question,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    lineHeight = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 2×2 option grid
                OptionsGrid(
                    options = currentQuestion.options,
                    selectedOption = selectedAnswer,
                    onOptionSelected = { option ->
                        answers[currentQuestion.id] = option
                        if (step == questions.lastIndex) {
                            pendingAdvance = true
                        } else {
                            // Short delay so the selection is visually acknowledged
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom footer: Back button + hint
            BottomFooter(
                step = step,
                isAnswered = selectedAnswer != null,
                onBack = { if (step > 0) step-- },
                onNext = {
                    if (selectedAnswer != null) {
                        if (step < questions.lastIndex) step++
                    }
                }
            )
        }
    }
}

// ---------- Sub-composables ----------

@Composable
private fun TopBar(onSkip: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Contab logo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.BarChart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Contab",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Skip button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(20.dp))
                .clickable { onSkip() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Omitir",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProgressBar(currentStep: Int, totalSteps: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until totalSteps) {
                val isCompleted = i < currentStep
                val isCurrent = i == currentStep

                val segmentWidth by animateDpAsState(
                    targetValue = if (isCurrent) 40.dp else 20.dp,
                    animationSpec = tween(durationMillis = 300),
                    label = "segmentWidth_$i"
                )
                val segmentAlpha by animateColorAsState(
                    targetValue = when {
                        isCompleted || isCurrent -> Color.White
                        else -> Color.White.copy(alpha = 0.25f)
                    },
                    animationSpec = tween(durationMillis = 300),
                    label = "segmentAlpha_$i"
                )

                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(segmentAlpha)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "${currentStep + 1}/$totalSteps",
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun OptionsGrid(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    // 2 rows × 2 columns
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (rowIndex in 0 until 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (colIndex in 0 until 2) {
                    val optionIndex = rowIndex * 2 + colIndex
                    val option = options[optionIndex]
                    val isSelected = option == selectedOption

                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else Color.White.copy(alpha = 0.12f),
                        animationSpec = tween(durationMillis = 200),
                        label = "optionBg_$optionIndex"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) Blue700 else Color.White,
                        animationSpec = tween(durationMillis = 200),
                        label = "optionText_$optionIndex"
                    )
                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) Color.Transparent else Color.White.copy(alpha = 0.30f),
                        animationSpec = tween(durationMillis = 200),
                        label = "optionBorder_$optionIndex"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable { onOptionSelected(option) }
                            .padding(vertical = 20.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            color = textColor,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomFooter(
    step: Int,
    isAnswered: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Back button (hidden on first step)
        if (step > 0) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(12.dp))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Hint / Next
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isAnswered) Color.White.copy(alpha = 0.18f)
                    else Color.White.copy(alpha = 0.08f)
                )
                .then(
                    if (isAnswered) Modifier.clickable { onNext() } else Modifier
                )
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isAnswered) "Continuar" else "Selecciona para continuar",
                color = if (isAnswered) Color.White else Color.White.copy(alpha = 0.45f),
                fontSize = 14.sp,
                fontWeight = if (isAnswered) FontWeight.SemiBold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}
