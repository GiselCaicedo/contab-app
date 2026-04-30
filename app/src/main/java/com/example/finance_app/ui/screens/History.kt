package com.example.finance_app.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finance_app.data.EntryMethod
import com.example.finance_app.data.FinanceData
import com.example.finance_app.data.Transaction
import com.example.finance_app.ui.Tokens
import com.example.finance_app.ui.iconForCategory

@Composable
fun HistoryScreen() {
    var search by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf("all") }

    val filtered = FinanceData.transactions.filter {
        it.concept.contains(search, ignoreCase = true) &&
            (activeFilter == "all" || it.catId == activeFilter)
    }
    val totalFiltered = filtered.sumOf { it.amount }
    val groups = FinanceData.groupByDate(filtered)
    val usedCats = FinanceData.categories.filter { c -> FinanceData.transactions.any { it.catId == c.id } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Tokens.Bg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Search
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Tokens.Surface)
                .border(1.dp, Tokens.Stroke, RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = Tokens.TextSec, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (search.isEmpty()) {
                    Text(
                        "Buscar gasto...",
                        color = Tokens.TextDim,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                BasicTextField(
                    value = search,
                    onValueChange = { search = it },
                    singleLine = true,
                    cursorBrush = SolidColor(Tokens.Accent),
                    textStyle = TextStyle(color = Tokens.TextMain, fontSize = 14.sp),
                )
            }
            if (search.isNotEmpty()) {
                Icon(
                    Icons.Outlined.Close, contentDescription = "Limpiar",
                    tint = Tokens.TextSec,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { search = "" }
                )
            }
        }

        // Filter chips
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip("Todos", activeFilter == "all") { activeFilter = "all" }
            usedCats.forEach { cat ->
                FilterChip(cat.label, activeFilter == cat.id) { activeFilter = cat.id }
            }
        }

        // Summary
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Tokens.Surface)
                .border(1.dp, Tokens.Stroke, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    "REGISTROS",
                    color = Tokens.TextDim,
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${filtered.size} movimientos",
                    color = Tokens.TextMain,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "TOTAL",
                    color = Tokens.TextDim,
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "-${FinanceData.formatCop(totalFiltered)}",
                    color = Tokens.Coral,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        groups.forEach { (date, txs) ->
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 18.dp)) {
                Text(
                    FinanceData.friendlyDate(date).uppercase(),
                    color = Tokens.TextSec,
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    txs.forEach { TxRow(it) }
                }
            }
        }
    }
}

@Composable
private fun FilterChip(label: String, active: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) Tokens.Accent else Tokens.Surface)
            .border(1.dp, if (active) Color.Transparent else Tokens.Stroke, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            color = if (active) Color.White else Tokens.TextSec,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium,
        )
    }
}

@Composable
private fun TxRow(tx: Transaction) {
    val cat = FinanceData.categoryById(tx.catId)
    val methodIcon = when (tx.method) {
        EntryMethod.SCAN -> Icons.Outlined.CameraAlt
        EntryMethod.VOICE -> Icons.Outlined.Mic
        EntryMethod.MANUAL -> Icons.Outlined.Edit
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Tokens.Surface)
            .border(1.dp, Tokens.Stroke, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(cat.light),
            contentAlignment = Alignment.Center,
        ) {
            Icon(iconForCategory(cat.id), contentDescription = null, tint = cat.color, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                tx.concept,
                color = Tokens.TextMain,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
            )
            Spacer(Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    cat.label,
                    color = Tokens.TextSec,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.width(6.dp))
                Icon(methodIcon, contentDescription = null, tint = Tokens.TextDim, modifier = Modifier.size(12.dp))
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "-${FinanceData.formatCop(tx.amount)}",
                color = Tokens.Coral,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = Tokens.TextSec, modifier = Modifier.size(14.dp))
                Icon(Icons.Outlined.Delete, contentDescription = "Borrar", tint = Tokens.Coral, modifier = Modifier.size(14.dp))
            }
        }
    }
}
