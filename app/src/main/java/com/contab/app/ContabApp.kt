package com.contab.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.contab.app.ui.components.ContabBottomNav
import com.contab.app.ui.components.ContabHeader
import com.contab.app.ui.components.FABMenu
import com.contab.app.ui.screens.*
import com.contab.app.ui.theme.*
import kotlinx.coroutines.delay

val MAIN_SCREENS = setOf("home", "history", "reports", "insights")

@Composable
fun ContabApp() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: ""

    var fabOpen by remember { mutableStateOf(false) }
    var toast  by remember { mutableStateOf<String?>(null) }

    val isMain = currentRoute in MAIN_SCREENS
    val isAuth = currentRoute in setOf("login", "register", "onboarding")

    // Toast auto-dismiss
    LaunchedEffect(toast) {
        if (toast != null) { delay(2200); toast = null }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Status bar color
            Box(modifier = Modifier.fillMaxWidth().windowInsetsTopHeight(WindowInsets.statusBars)
                .background(if (!isAuth) Blue500 else Blue500))

            // App header (only for main + categories screens)
            if (isMain || currentRoute == "categories") {
                ContabHeader()
            }

            // Secondary title bar for non-home main screens
            if (isMain && currentRoute != "home") {
                Row(
                    modifier = Modifier.fillMaxWidth().background(SurfaceColor)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        when (currentRoute) {
                            "history"  -> "Historial"
                            "reports"  -> "Reportes"
                            "insights" -> "Insights"
                            else -> ""
                        },
                        fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextColor, fontFamily = Poppins
                    )
                    if (currentRoute == "history") {
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(Blue50)
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text("Categorías", fontSize = 11.sp, color = Blue500,
                                fontWeight = FontWeight.SemiBold, fontFamily = Poppins)
                        }
                    }
                }
            }
            if (currentRoute == "categories") {
                Row(
                    modifier = Modifier.fillMaxWidth().background(SurfaceColor)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("Categorías", fontSize = 17.sp, fontWeight = FontWeight.Bold,
                        color = TextColor, fontFamily = Poppins)
                }
            }

            // Screen content
            Box(modifier = Modifier.weight(1f)) {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login")      { LoginScreen(navController) }
                    composable("register")   { RegisterScreen(navController) }
                    composable("onboarding") { OnboardingScreen(navController) }

                    composable("home")       { DashboardScreen(navController) }
                    composable("history")    { HistoryScreen() }
                    composable("reports")    { ReportsScreen() }
                    composable("insights")   { InsightsScreen() }
                    composable("categories") { CategoriesScreen() }

                    composable("scan")   { ScanScreen(navController) }
                    composable("voice")  { VoiceScreen(navController) }
                    composable("manual") { ManualScreen(navController) }
                }

                // FAB overlay
                FABMenu(
                    visible = fabOpen,
                    onClose = { fabOpen = false },
                    onOptionSelected = { id ->
                        fabOpen = false
                        navController.navigate(id)
                    }
                )

                // Toast
                if (toast != null) {
                    Box(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)
                            .clip(RoundedCornerShape(22.dp)).background(TextColor)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Check, null, tint = SuccessColor, modifier = Modifier.size(14.dp))
                            Text(toast ?: "", fontSize = 12.sp, color = SurfaceColor,
                                fontWeight = FontWeight.Medium, fontFamily = Poppins)
                        }
                    }
                }
            }

            // Bottom nav
            if (isMain) {
                ContabBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        fabOpen = false
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onFabClick = { fabOpen = !fabOpen }
                )
                // Home indicator
                Box(
                    modifier = Modifier.fillMaxWidth().background(SurfaceColor)
                        .windowInsetsBottomHeight(WindowInsets.navigationBars),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(modifier = Modifier.padding(top = 4.dp)
                        .size(100.dp, 4.dp).clip(RoundedCornerShape(2.dp)).background(BorderColor))
                }
            }
        }
    }
}
