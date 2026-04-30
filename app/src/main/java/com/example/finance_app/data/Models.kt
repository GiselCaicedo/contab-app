package com.example.finance_app.data

import androidx.compose.ui.graphics.Color
import java.text.NumberFormat
import java.util.Locale

enum class EntryMethod { SCAN, VOICE, MANUAL }

data class Category(
    val id: String,
    val label: String,
    val color: Color,
    val light: Color,
)

data class Transaction(
    val id: Int,
    val concept: String,
    val amount: Long,
    val catId: String,
    val date: String,
    val method: EntryMethod,
)

object FinanceData {
    val categories: List<Category> = listOf(
        Category("food",          "Alimentación",   Color(0xFFFF6B8A), Color(0x26FF6B8A)),
        Category("transport",     "Transporte",      Color(0xFF4DA8FF), Color(0x264DA8FF)),
        Category("services",      "Servicios",       Color(0xFFFFB84D), Color(0x26FFB84D)),
        Category("health",        "Salud",           Color(0xFF34E0A1), Color(0x2634E0A1)),
        Category("entertainment", "Entretenimiento", Color(0xFFC77DFF), Color(0x26C77DFF)),
        Category("shopping",      "Compras",         Color(0xFF7B61FF), Color(0x267B61FF)),
        Category("education",     "Educación",       Color(0xFF38BDF8), Color(0x2638BDF8)),
        Category("other",         "Otros",           Color(0xFF94A3B8), Color(0x2694A3B8)),
    )

    val transactions: List<Transaction> = listOf(
        Transaction(1,  "Restaurante La Candelaria", 45000,  "food",          "2026-04-19", EntryMethod.SCAN),
        Transaction(2,  "TransMilenio",              2800,   "transport",     "2026-04-19", EntryMethod.VOICE),
        Transaction(3,  "Netflix",                   37900,  "entertainment", "2026-04-18", EntryMethod.MANUAL),
        Transaction(4,  "Farmacia Cruz Verde",       28500,  "health",        "2026-04-18", EntryMethod.SCAN),
        Transaction(5,  "Rappi Market",              89000,  "shopping",      "2026-04-17", EntryMethod.SCAN),
        Transaction(6,  "Luz EPM",                   142000, "services",      "2026-04-16", EntryMethod.MANUAL),
        Transaction(7,  "Café Pergamino",            18000,  "food",          "2026-04-15", EntryMethod.SCAN),
        Transaction(8,  "Uber",                      22000,  "transport",     "2026-04-15", EntryMethod.VOICE),
        Transaction(9,  "Curso Platzi",              59000,  "education",     "2026-04-14", EntryMethod.MANUAL),
        Transaction(10, "Supermercado Éxito",        215000, "shopping",      "2026-04-13", EntryMethod.SCAN),
    )

    fun categoryById(id: String): Category =
        categories.firstOrNull { it.id == id } ?: categories.last()

    private val cop: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }

    fun formatCop(n: Long): String = cop.format(n)

    fun friendlyDate(date: String): String = when (date) {
        "2026-04-19" -> "Hoy"
        "2026-04-18" -> "Ayer"
        else -> {
            val parts = date.split("-")
            val month = listOf(
                "enero","febrero","marzo","abril","mayo","junio",
                "julio","agosto","septiembre","octubre","noviembre","diciembre"
            )[parts[1].toInt() - 1]
            "${parts[2].toInt()} de $month"
        }
    }

    fun groupByDate(txs: List<Transaction>): List<Pair<String, List<Transaction>>> =
        txs.groupBy { it.date }.toList().sortedByDescending { it.first }
}
