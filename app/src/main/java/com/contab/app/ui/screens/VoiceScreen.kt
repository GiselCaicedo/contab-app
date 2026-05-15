package com.contab.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.contab.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun VoiceScreen(navController: NavController) {
    var phase by remember { mutableStateOf("idle") }
    val transcript = "Gasté cuarenta y cinco mil pesos en el restaurante La Candelaria"

    LaunchedEffect(phase) {
        if (phase == "recording") {
            delay(2500)
            phase = "result"
        }
    }

    val pulseScale by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulseScale"
    )

    Column(
        modifier = Modifier.fillMaxSize().background(BgColor).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp).clip(RoundedCornerShape(10.dp))
                    .background(SurfaceColor).clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ArrowBack, null, tint = TextColor, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(12.dp))
            Text("Ingreso por voz", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextColor)
        }

        Spacer(Modifier.weight(0.3f))

        // Waveform
        Row(
            modifier = Modifier.height(64.dp).padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(2.5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(28) { i ->
                val barHeight = if (phase == "recording") {
                    (10f + abs(sin(i * 0.7)) * 42).dp
                } else {
                    5.dp
                }
                val animated by animateDpAsState(targetValue = barHeight, animationSpec = tween(150), label = "bar$i")
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(animated)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (phase == "recording") Blue500 else BorderColor)
                )
            }
        }

        Spacer(Modifier.height(36.dp))

        // Mic button
        Box(contentAlignment = Alignment.Center) {
            if (phase == "recording") {
                Box(
                    modifier = Modifier
                        .size((84 * pulseScale).dp)
                        .clip(CircleShape)
                        .background(DangerColor.copy(alpha = 0.12f))
                )
            }
            Box(
                modifier = Modifier
                    .size(84.dp).clip(CircleShape)
                    .background(if (phase == "recording") DangerColor else Blue500)
                    .clickable { if (phase == "idle") phase = "recording" },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Mic, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
        }

        Spacer(Modifier.height(24.dp))

        // State text
        Text(
            text = when (phase) {
                "idle" -> "Di algo como:\n\"Gasté 45 mil en el supermercado\""
                "recording" -> "Escuchando..."
                else -> "Entendido. Confirma el registro:"
            },
            fontSize = 13.sp, color = TextSecColor, textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        // Result card
        if (phase == "result") {
            Spacer(Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Transcripción", fontSize = 11.sp, color = TextSecColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 6.dp))
                    Row(
                        modifier = Modifier
                            .border(2.dp, Blue500, RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp))
                            .padding(start = 10.dp, bottom = 16.dp)
                    ) {
                        Text("\"$transcript\"", fontSize = 13.sp, color = TextColor,
                            fontStyle = FontStyle.Italic, lineHeight = 20.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("$ 45.000" to "Monto", "Restaurante" to "Concepto", "Alimentos" to "Categoría").forEach { (val_, lbl) ->
                            Column(
                                modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp))
                                    .background(BgColor).padding(8.dp, 8.dp)
                            ) {
                                Text(lbl, fontSize = 10.sp, color = TextSecColor,
                                    modifier = Modifier.padding(bottom = 2.dp))
                                Text(val_, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextColor)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        if (phase == "result") {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp).height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text("Guardar gasto", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Spacer(Modifier.height(32.dp))
        }
    }
}
