package com.contab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.contab.app.data.ALL_CATEGORIES
import com.contab.app.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ManualScreen(navController: NavController) {
    var amount  by remember { mutableStateOf("") }
    var concept by remember { mutableStateOf("") }
    var date    by remember { mutableStateOf("2026-04-19") }
    var catId   by remember { mutableStateOf("food") }

    Column(
        modifier = Modifier.fillMaxSize().background(BgColor).verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                    .background(SurfaceColor).clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ArrowBack, null, tint = TextColor, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(12.dp))
            Text("Nuevo gasto", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextColor, fontFamily = Poppins)
        }

        // Amount
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¿CUÁNTO GASTASTE?", fontSize = 11.sp, color = TextSecColor,
                fontWeight = FontWeight.Medium, letterSpacing = 0.4.sp,
                fontFamily = Poppins, modifier = Modifier.padding(bottom = 10.dp))

            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text("$", fontSize = 26.sp, color = TextSecColor, fontWeight = FontWeight.Light,
                    fontFamily = Poppins, modifier = Modifier.padding(bottom = 4.dp))
                Spacer(Modifier.width(4.dp))
                BasicTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        fontSize = 44.sp, fontWeight = FontWeight.Bold, color = TextColor,
                        fontFamily = Poppins, textAlign = TextAlign.Center
                    ),
                    decorationBox = { inner ->
                        Box(modifier = Modifier.width(190.dp), contentAlignment = Alignment.Center) {
                            if (amount.isEmpty()) {
                                Text("0", fontSize = 44.sp, fontWeight = FontWeight.Bold,
                                    color = TextSecColor.copy(alpha = 0.3f),
                                    fontFamily = Poppins, textAlign = TextAlign.Center)
                            }
                            inner()
                        }
                    }
                )
                Spacer(Modifier.width(4.dp))
                Text("COP", fontSize = 14.sp, color = TextSecColor, fontFamily = Poppins,
                    modifier = Modifier.padding(bottom = 4.dp))
            }

            Box(
                modifier = Modifier
                    .width(120.dp).height(2.dp).clip(RoundedCornerShape(2.dp))
                    .background(Blue500.copy(alpha = 0.6f))
                    .padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        // Form cards
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Concepto
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp)) {
                    Text("Concepto", fontSize = 11.sp, color = TextSecColor,
                        fontWeight = FontWeight.Medium, fontFamily = Poppins,
                        modifier = Modifier.padding(bottom = 5.dp))
                    BasicTextField(
                        value = concept,
                        onValueChange = { concept = it },
                        textStyle = TextStyle(fontSize = 14.sp, color = TextColor, fontFamily = Poppins),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { inner ->
                            Box {
                                if (concept.isEmpty()) Text("Ej: Almuerzo en restaurante",
                                    fontSize = 14.sp, color = TextSecColor.copy(alpha = 0.5f), fontFamily = Poppins)
                                inner()
                            }
                        }
                    )
                }
            }

            // Fecha
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Outlined.CalendarMonth, null, tint = TextSecColor, modifier = Modifier.size(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Fecha", fontSize = 11.sp, color = TextSecColor,
                            fontWeight = FontWeight.Medium, fontFamily = Poppins,
                            modifier = Modifier.padding(bottom = 3.dp))
                        BasicTextField(
                            value = date,
                            onValueChange = { date = it },
                            textStyle = TextStyle(fontSize = 13.sp, color = TextColor, fontFamily = Poppins),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Categoría
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Categoría", fontSize = 11.sp, color = TextSecColor,
                        fontWeight = FontWeight.Medium, fontFamily = Poppins,
                        modifier = Modifier.padding(bottom = 10.dp))
                    androidx.compose.foundation.layout.FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        ALL_CATEGORIES.forEach { cat ->
                            val active = catId == cat.id
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        if (active) 1.5.dp else 1.dp,
                                        if (active) cat.color else BorderColor,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .background(if (active) cat.lightColor else Color.Transparent)
                                    .clickable { catId = cat.id }
                                    .padding(horizontal = 10.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(cat.label, fontSize = 11.sp,
                                    color = if (active) cat.color else TextSecColor,
                                    fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                                    fontFamily = Poppins)
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text("Guardar gasto", fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = Poppins)
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
