package com.contab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.WbIncandescent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contab.app.data.ALL_CATEGORIES
import com.contab.app.data.Category
import com.contab.app.data.SAMPLE_TRANSACTIONS
import com.contab.app.data.Transaction
import com.contab.app.data.TransactionMethod
import com.contab.app.data.formatCOP
import com.contab.app.data.friendlyDate
import com.contab.app.data.getCategoryById
import com.contab.app.ui.theme.BgColor
import com.contab.app.ui.theme.Blue100
import com.contab.app.ui.theme.Blue50
import com.contab.app.ui.theme.Blue500
import com.contab.app.ui.theme.Blue600
import com.contab.app.ui.theme.Blue700
import com.contab.app.ui.theme.DangerColor
import com.contab.app.ui.theme.SurfaceColor
import com.contab.app.ui.theme.TextColor
import com.contab.app.ui.theme.TextSecColor

// ---------------------------------------------------------------------------
// Private helpers (local to HistoryScreen)
// ---------------------------------------------------------------------------

private fun historyCategoryIcon(id: String): ImageVector = when (id) {
    "food"          -> Icons.Outlined.Fastfood
    "transport"     -> Icons.Outlined.Train
    "services"      -> Icons.Outlined.WbIncandescent
    "health"        -> Icons.Outlined.LocalHospital
    "entertainment" -> Icons.Outlined.Movie
    "shopping"      -> Icons.Outlined.ShoppingBag
    "education"     -> Icons.Outlined.School
    else            -> Icons.Outlined.ShoppingCart
}

private fun historyMethodIcon(method: TransactionMethod): ImageVector = when (method) {
    TransactionMethod.SCAN   -> Icons.Outlined.CameraAlt
    TransactionMethod.VOICE  -> Icons.Outlined.MicNone
    TransactionMethod.MANUAL -> Icons.Outlined.Edit
}

private fun historyMethodLabel(method: TransactionMethod): String = when (method) {
    TransactionMethod.SCAN   -> "Escaneado"
    TransactionMethod.VOICE  -> "Voz"
    TransactionMethod.MANUAL -> "Manual"
}

// ---------------------------------------------------------------------------
// HistoryScreen
// ---------------------------------------------------------------------------
@Composable
fun HistoryScreen() {
    var searchQuery   by remember { mutableStateOf("") }
    var activeFilter  by remember { mutableStateOf("Todos") }

    // Categories that appear in the sample data
    val usedCategories: List<Category> = remember {
        val usedIds = SAMPLE_TRANSACTIONS.map { it.categoryId }.toSet()
        ALL_CATEGORIES.filter { it.id in usedIds }
    }

    // Filter chips list: "Todos" + one per used category
    val filterChips: List<String> = remember(usedCategories) {
        listOf("Todos") + usedCategories.map { it.label }
    }

    // Filtered transactions
    val filtered = remember(searchQuery, activeFilter) {
        SAMPLE_TRANSACTIONS.filter { tx ->
            val matchesSearch = searchQuery.isBlank() ||
                tx.concept.contains(searchQuery, ignoreCase = true)
            val matchesFilter = activeFilter == "Todos" ||
                getCategoryById(tx.categoryId).label == activeFilter
            matchesSearch && matchesFilter
        }
    }

    val totalFiltered = remember(filtered) { filtered.sumOf { it.amount } }

    // Group by date, descending
    val grouped = remember(filtered) {
        filtered
            .groupBy { it.date }
            .entries
            .sortedByDescending { it.key }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor),
    ) {
        // ── Search bar ────────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .padding(top = 14.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(14.dp), spotColor = Blue50, ambientColor = Blue100)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceColor)
                    .padding(horizontal = 12.dp, vertical = 11.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = TextSecColor,
                        modifier = Modifier.size(18.dp)
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Buscar movimiento…",
                                fontSize = 13.sp,
                                color = TextSecColor,
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                color = TextColor,
                                fontWeight = FontWeight.Normal,
                            ),
                            cursorBrush = SolidColor(Blue500),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Limpiar",
                            tint = TextSecColor,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { searchQuery = "" }
                        )
                    }
                }
            }
        }

        // ── Filter chips ──────────────────────────────────────────────────────
        item {
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                filterChips.forEach { chip ->
                    val isActive = chip == activeFilter
                    val category = usedCategories.find { it.label == chip }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isActive) Blue500 else SurfaceColor)
                            .then(
                                if (!isActive) Modifier.border(
                                    1.dp, Blue50, RoundedCornerShape(20.dp)
                                ) else Modifier
                            )
                            .clickable { activeFilter = chip }
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        if (category != null) {
                            Icon(
                                imageVector = historyCategoryIcon(category.id),
                                contentDescription = null,
                                tint = if (isActive) Color.White else TextSecColor,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = chip,
                            fontSize = 12.sp,
                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isActive) Color.White else TextSecColor,
                        )
                    }
                }
            }
        }

        // ── Summary row ───────────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 14.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceColor)
                    .border(1.dp, Blue50, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${filtered.size} registro${if (filtered.size != 1) "s" else ""}",
                    fontSize = 12.sp,
                    color = TextSecColor,
                )
                Text(
                    text = "–${formatCOP(totalFiltered)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue700,
                )
            }
        }

        // ── Transaction groups ────────────────────────────────────────────────
        grouped.forEach { (date, txs) ->
            item {
                HistoryDateGroup(date = date, transactions = txs)
                Spacer(Modifier.height(18.dp))
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

// ---------------------------------------------------------------------------
// Date group
// ---------------------------------------------------------------------------
@Composable
private fun HistoryDateGroup(
    date: String,
    transactions: List<Transaction>,
) {
    val isToday    = date == "2026-04-19"
    val groupTotal = transactions.sumOf { it.amount }
    val day        = date.split("-").getOrElse(2) { "?" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
                )
                Text(
                    text = "–${formatCOP(groupTotal)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue700,
                )
            }

            Text(
                text = "${transactions.size} movimiento${if (transactions.size != 1) "s" else ""}",
                fontSize = 10.sp,
                color = TextSecColor,
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
                        HistoryTransactionRow(tx = tx)
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

// ---------------------------------------------------------------------------
// Transaction row with edit + delete actions
// ---------------------------------------------------------------------------
@Composable
private fun HistoryTransactionRow(tx: Transaction) {
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
                imageVector = historyCategoryIcon(tx.categoryId),
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
                        fontWeight = FontWeight.Medium,
                    )
                }
                // Method icon + label
                Icon(
                    imageVector = historyMethodIcon(tx.method),
                    contentDescription = null,
                    tint = TextSecColor,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    text = historyMethodLabel(tx.method),
                    fontSize = 9.5.sp,
                    color = TextSecColor,
                )
            }
        }

        // Amount + action buttons column
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "–${formatCOP(tx.amount)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // Edit button
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Blue50)
                        .clickable { /* edit action */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Editar",
                        tint = Blue600,
                        modifier = Modifier.size(13.dp)
                    )
                }
                // Delete button
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(DangerColor.copy(alpha = 0.08f))
                        .clickable { /* delete action */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Eliminar",
                        tint = DangerColor,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
