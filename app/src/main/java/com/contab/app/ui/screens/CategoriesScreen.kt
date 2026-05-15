package com.contab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.WbIncandescent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contab.app.data.ALL_CATEGORIES
import com.contab.app.data.Category
import com.contab.app.data.SAMPLE_TRANSACTIONS
import com.contab.app.data.formatCOP
import com.contab.app.ui.theme.BgColor
import com.contab.app.ui.theme.Blue100
import com.contab.app.ui.theme.Blue200
import com.contab.app.ui.theme.Blue50
import com.contab.app.ui.theme.Blue500
import com.contab.app.ui.theme.Blue600
import com.contab.app.ui.theme.Blue700
import com.contab.app.ui.theme.BorderColor
import com.contab.app.ui.theme.Poppins
import com.contab.app.ui.theme.SurfaceColor
import com.contab.app.ui.theme.TextColor
import com.contab.app.ui.theme.TextSecColor

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------
private fun iconForCategory(id: String): ImageVector = when (id) {
    "food"          -> Icons.Outlined.Fastfood
    "transport"     -> Icons.Outlined.Train
    "services"      -> Icons.Outlined.WbIncandescent
    "health"        -> Icons.Outlined.LocalHospital
    "entertainment" -> Icons.Outlined.Movie
    "shopping"      -> Icons.Outlined.ShoppingBag
    "education"     -> Icons.Outlined.School
    else            -> Icons.Outlined.ShoppingCart
}

private val COLOR_PALETTE = listOf(
    Color(0xFF0F53F0), // Blue500
    Color(0xFF0A3599), // Blue700
    Color(0xFF6691F5), // Blue300
    Color(0xFF00B87A), // Green
    Color(0xFFF59E0B), // Amber
    Color(0xFFE8445A), // Red
    Color(0xFFAD1FFF), // Purple
    Color(0xFF0EA5E9), // Sky
)

// ---------------------------------------------------------------------------
// CategoriesScreen
// ---------------------------------------------------------------------------
@Composable
fun CategoriesScreen() {
    val categories = remember { mutableStateListOf<Category>().apply { addAll(ALL_CATEGORIES) } }
    var adding by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(COLOR_PALETTE[0]) }

    val spendByCategory = remember {
        SAMPLE_TRANSACTIONS
            .groupBy { it.categoryId }
            .mapValues { (_, txs) -> txs.sumOf { it.amount } }
    }
    val grandTotal = remember { spendByCategory.values.sum() }
    val totalMovements = SAMPLE_TRANSACTIONS.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .padding(top = 14.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${categories.size} activas · organiza tus gastos",
                    fontSize = 11.sp,
                    color = TextSecColor,
                    fontFamily = Poppins
                )
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(10.dp),
                            ambientColor = Blue500.copy(alpha = 0.25f),
                            spotColor = Blue500.copy(alpha = 0.25f)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(Blue500)
                        .clickable { adding = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Nueva",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontFamily = Poppins
                        )
                    }
                }
            }
        }

        // ── Add new category form ─────────────────────────────────────────────
        if (adding) {
            item {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
                        .fillMaxWidth()
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = Blue50,
                            spotColor = Blue100
                        )
                        .clip(RoundedCornerShape(18.dp))
                        .background(SurfaceColor)
                        .border(1.5.dp, Blue200, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Nueva categoría",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            fontFamily = Poppins
                        )

                        // Name field with placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Blue50.copy(alpha = 0.60f))
                                .border(1.dp, Blue100, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            if (newName.isEmpty()) {
                                Text(
                                    text = "Nombre de la categoría",
                                    fontSize = 13.sp,
                                    color = TextSecColor.copy(alpha = 0.55f),
                                    fontFamily = Poppins
                                )
                            }
                            BasicTextField(
                                value = newName,
                                onValueChange = { newName = it },
                                textStyle = TextStyle(
                                    fontSize = 13.sp,
                                    color = TextColor,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Medium
                                ),
                                cursorBrush = SolidColor(Blue500),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Color palette
                        Text(
                            text = "Color",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecColor,
                            fontFamily = Poppins
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            COLOR_PALETTE.forEach { color ->
                                val isSelected = color == selectedColor
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .then(
                                            if (isSelected) Modifier.border(2.5.dp, Color.White, CircleShape)
                                            else Modifier
                                        )
                                        .clickable { selectedColor = color },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(Color.White)
                                        )
                                    }
                                }
                            }
                        }

                        // Cancel + Save
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                                    .clickable {
                                        adding = false
                                        newName = ""
                                        selectedColor = COLOR_PALETTE[0]
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Cancelar",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecColor,
                                    fontFamily = Poppins
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Blue500)
                                    .clickable {
                                        if (newName.isNotBlank()) {
                                            categories.add(
                                                Category(
                                                    id = "custom_${System.currentTimeMillis()}",
                                                    label = newName.trim(),
                                                    color = selectedColor,
                                                    lightColor = selectedColor.copy(alpha = 0.12f)
                                                )
                                            )
                                            newName = ""
                                            selectedColor = COLOR_PALETTE[0]
                                            adding = false
                                        }
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Guardar",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    fontFamily = Poppins
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Summary band ──────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Blue700)
            ) {
                // Decorative circle
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = 30.dp, y = (-20).dp)
                        .align(Alignment.CenterEnd)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.06f))
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total clasificado",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.70f),
                            fontFamily = Poppins
                        )
                        Text(
                            text = formatCOP(grandTotal),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = Poppins
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Movimientos",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.70f),
                            fontFamily = Poppins
                        )
                        Text(
                            text = "$totalMovements",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = Poppins
                        )
                    }
                }
            }
        }

        // ── Category list ─────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp), ambientColor = Blue50, spotColor = Blue100)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceColor)
            ) {
                Column {
                    val catList = categories.toList()
                    catList.forEachIndexed { idx, cat ->
                        val amtSpent = spendByCategory[cat.id] ?: 0L
                        val txCount = SAMPLE_TRANSACTIONS.count { it.categoryId == cat.id }
                        val maxSpend = spendByCategory.values.maxOrNull() ?: 1L
                        val progress = (amtSpent.toFloat() / maxSpend.toFloat()).coerceIn(0f, 1f)
                        val isLast = idx == catList.lastIndex

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 11.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Icon container with color dot badge
                                Box(modifier = Modifier.size(38.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(11.dp))
                                            .background(Blue50),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = iconForCategory(cat.id),
                                            contentDescription = null,
                                            tint = Blue600,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    // Color dot badge bottom-right with white border
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .align(Alignment.BottomEnd)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                            .padding(1.5.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape)
                                                .background(cat.color)
                                        )
                                    }
                                }

                                // Content column
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = cat.label,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextColor,
                                            fontFamily = Poppins
                                        )
                                        Text(
                                            text = if (amtSpent > 0) formatCOP(amtSpent) else "—",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextColor,
                                            fontFamily = Poppins
                                        )
                                    }
                                    Spacer(Modifier.height(5.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        // Progress bar
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(Blue50)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(progress)
                                                    .fillMaxHeight()
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(Blue500)
                                            )
                                        }
                                        Text(
                                            text = "$txCount ${if (txCount == 1) "gasto" else "gastos"}",
                                            fontSize = 10.sp,
                                            color = TextSecColor,
                                            fontFamily = Poppins
                                        )
                                    }
                                }

                                // Edit + Delete icon buttons
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Blue50)
                                            .clickable { /* TODO: open edit dialog */ },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = "Editar",
                                            tint = Blue600,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFFEF2F4))
                                            .clickable { categories.remove(cat) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFE8445A),
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }

                            // Divider between rows
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
}
