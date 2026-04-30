package com.example.finance_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finance_app.ui.screens.AddManualScreen
import com.example.finance_app.ui.screens.AddScanScreen
import com.example.finance_app.ui.screens.AddVoiceScreen
import com.example.finance_app.ui.screens.CategoriesScreen
import com.example.finance_app.ui.screens.DashboardScreen
import com.example.finance_app.ui.screens.HistoryScreen
import com.example.finance_app.ui.screens.InsightsScreen
import com.example.finance_app.ui.screens.ReportsScreen
import kotlinx.coroutines.delay

@Composable
fun FinanceApp() {
    var screen by remember { mutableStateOf(Screen.HOME) }
    var addFlow by remember { mutableStateOf<AddFlow?>(null) }
    var fabOpen by remember { mutableStateOf(false) }
    var toast by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(toast) {
        if (toast != null) { delay(2200); toast = null }
    }

    val isAddFlow = addFlow != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Tokens.Bg)
    ) {
        if (!isAddFlow) {
            TopHeader()
            if (screen != Screen.HOME) {
                ScreenTitle(screen) { screen = Screen.CATEGORIES }
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (addFlow) {
                AddFlow.SCAN -> AddScanScreen(
                    onBack = { addFlow = null },
                    onDone = { addFlow = null; toast = "Gasto guardado correctamente" }
                )
                AddFlow.VOICE -> AddVoiceScreen(
                    onBack = { addFlow = null },
                    onDone = { addFlow = null; toast = "Gasto guardado correctamente" }
                )
                AddFlow.MANUAL -> AddManualScreen(
                    onBack = { addFlow = null },
                    onDone = { addFlow = null; toast = "Gasto guardado correctamente" }
                )
                null -> when (screen) {
                    Screen.HOME -> DashboardScreen(onNavigate = { screen = it })
                    Screen.HISTORY -> HistoryScreen()
                    Screen.REPORTS -> ReportsScreen()
                    Screen.INSIGHTS -> InsightsScreen()
                    Screen.CATEGORIES -> CategoriesScreen()
                }
            }

            if (fabOpen && !isAddFlow) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    FabMenu(
                        onSelect = { fabOpen = false; addFlow = it },
                        onClose = { fabOpen = false }
                    )
                }
            }

            toast?.let { msg ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 96.dp)
                            .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = Tokens.Mint)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Tokens.SurfaceHi)
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(Tokens.Mint),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Outlined.Check, contentDescription = null, tint = androidx.compose.ui.graphics.Color.Black, modifier = Modifier.size(14.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(msg, color = Tokens.TextMain, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        if (!isAddFlow) {
            BottomNav(
                current = screen,
                onSelect = { screen = it; fabOpen = false },
                onFab = { fabOpen = !fabOpen },
                fabOpen = fabOpen,
            )
        }
    }
}
