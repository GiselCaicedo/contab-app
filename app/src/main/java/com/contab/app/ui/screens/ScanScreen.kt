package com.contab.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.contab.app.data.*
import com.contab.app.ui.theme.*
import kotlinx.coroutines.delay

private val DarkBg = Color(0xFF08081A)

@Composable
fun ScanScreen(navController: NavController) {
    var phase by remember { mutableStateOf("camera") }
    if (phase == "result") {
        ScanResultScreen(navController)
    } else {
        ScanCameraScreen(onCancel = { navController.popBackStack() }, onScanned = { phase = "result" })
    }
}

@Composable
private fun ScanCameraScreen(onCancel: () -> Unit, onScanned: () -> Unit) {
    var scanning by remember { mutableStateOf(false) }

    LaunchedEffect(scanning) {
        if (scanning) {
            delay(1800)
            onScanned()
        }
    }

    val scanOffset by rememberInfiniteTransition(label = "scan").animateFloat(
        initialValue = -90f, targetValue = 90f, animationSpec = infiniteRepeatable(
            tween(1200, easing = LinearEasing), RepeatMode.Reverse
        ), label = "scanLine"
    )

    Box(
        modifier = Modifier.fillMaxSize().background(DarkBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .clickable { onCancel() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text("Escanear recibo", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                // Viewfinder placeholder
                Box(
                    modifier = Modifier
                        .size(150.dp, 200.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Filled.CameraAlt, null, tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(32.dp))
                        Text("Coloca el recibo\ndentro del marco", color = Color.White.copy(alpha = 0.4f),
                            fontSize = 11.sp, textAlign = TextAlign.Center, lineHeight = 16.sp)
                    }
                }

                // Corner markers (L-shaped using nested borders)
                Box(modifier = Modifier.size(150.dp, 200.dp)) {
                    // Top-left
                    Box(modifier = Modifier.size(28.dp).align(Alignment.TopStart)
                        .border(androidx.compose.foundation.BorderStroke(2.dp, Blue500))
                        .padding(end = 26.dp, bottom = 26.dp).background(Color.Transparent))
                    // Top-right
                    Box(modifier = Modifier.size(28.dp).align(Alignment.TopEnd)
                        .border(androidx.compose.foundation.BorderStroke(2.dp, Blue500))
                        .padding(start = 26.dp, bottom = 26.dp).background(Color.Transparent))
                    // Bottom-left
                    Box(modifier = Modifier.size(28.dp).align(Alignment.BottomStart)
                        .border(androidx.compose.foundation.BorderStroke(2.dp, Blue500))
                        .padding(end = 26.dp, top = 26.dp).background(Color.Transparent))
                    // Bottom-right
                    Box(modifier = Modifier.size(28.dp).align(Alignment.BottomEnd)
                        .border(androidx.compose.foundation.BorderStroke(2.dp, Blue500))
                        .padding(start = 26.dp, top = 26.dp).background(Color.Transparent))
                }

                // Scan line
                if (scanning) {
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(2.dp)
                            .offset(y = scanOffset.dp)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    listOf(Color.Transparent, Blue500, Color.Transparent)
                                )
                            )
                    )
                }

                // Hint text
                Text(
                    "IA extrae fecha · monto · concepto · establecimiento",
                    color = Color.White.copy(alpha = 0.45f),
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Shutter
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .background(if (scanning) Blue500 else Color.White)
                        .clickable { if (!scanning) scanning = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.CameraAlt, null,
                        tint = if (scanning) Color.White else Color(0xFF0A0A1A),
                        modifier = Modifier.size(26.dp))
                }
                TextButton(onClick = { onCancel() }) {
                    Text("Cancelar", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ScanResultScreen(navController: NavController) {
    var amount   by remember { mutableStateOf("45000") }
    var concept  by remember { mutableStateOf("Restaurante La Candelaria") }
    var date     by remember { mutableStateOf("2026-04-19") }
    var selectedCat by remember { mutableStateOf("food") }

    Column(
        modifier = Modifier.fillMaxSize().background(BgColor).verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceColor)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ArrowBack, null, tint = TextColor, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(12.dp))
            Text("Datos extraídos", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextColor)
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFECFDF5))
                    .padding(horizontal = 11.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(Icons.Filled.Check, null, tint = SuccessColor, modifier = Modifier.size(12.dp))
                Text("4 campos detectados", color = SuccessColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Fields card
        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                listOf(
                    Triple("Monto (COP)", amount) { v: String -> amount = v },
                    Triple("Concepto", concept) { v: String -> concept = v },
                    Triple("Fecha", date) { v: String -> date = v },
                ).forEach { (label, value, onChange) ->
                    Column(modifier = Modifier.padding(bottom = 14.dp)) {
                        Text(label, fontSize = 11.sp, color = TextSecColor, fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 5.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(BgColor)
                                .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                                .padding(horizontal = 13.dp, vertical = 10.dp)
                        ) {
                            androidx.compose.foundation.text.BasicTextField(
                                value = value, onValueChange = onChange,
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 14.sp, color = TextColor
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Text("Categoría", fontSize = 11.sp, color = TextSecColor, fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp))
                androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    ALL_CATEGORIES.take(6).forEach { cat ->
                        val active = selectedCat == cat.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .border(if (active) 1.5.dp else 1.dp,
                                    if (active) cat.color else BorderColor, RoundedCornerShape(20.dp))
                                .background(if (active) cat.lightColor else SurfaceColor)
                                .clickable { selectedCat = cat.id }
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(cat.label, fontSize = 11.sp,
                                color = if (active) cat.color else TextSecColor,
                                fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue500),
            elevation = ButtonDefaults.buttonElevation(6.dp)
        ) {
            Text("Guardar gasto", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
    }
}
