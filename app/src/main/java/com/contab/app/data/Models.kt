package com.contab.app.data

import androidx.compose.ui.graphics.Color
import com.contab.app.ui.theme.*

enum class TransactionMethod { SCAN, VOICE, MANUAL }

data class Transaction(
    val id: Int,
    val concept: String,
    val amount: Long,
    val categoryId: String,
    val date: String,
    val method: TransactionMethod,
)

data class Category(
    val id: String,
    val label: String,
    val color: Color,
    val lightColor: Color,
)

val ALL_CATEGORIES = listOf(
    Category("food",          "Alimentación",   CatFood,      CatFoodLight),
    Category("transport",     "Transporte",      CatTransport,  CatTransportLight),
    Category("services",      "Servicios",       CatServices,   CatServicesLight),
    Category("health",        "Salud",           CatHealth,     CatHealthLight),
    Category("entertainment", "Entretenimiento", CatEntertain,  CatEntertainLight),
    Category("shopping",      "Compras",         CatShopping,   CatShoppingLight),
    Category("education",     "Educación",       CatEducation,  CatEducationLight),
    Category("other",         "Otros",           CatOther,      CatOtherLight),
)

fun getCategoryById(id: String) = ALL_CATEGORIES.find { it.id == id } ?: ALL_CATEGORIES.last()

fun formatCOP(amount: Long): String {
    val formatted = "%,d".format(amount).replace(',', '.')
    return "$ $formatted"
}

fun friendlyDate(date: String): String = when (date) {
    "2026-04-19" -> "Hoy"
    "2026-04-18" -> "Ayer"
    else -> {
        val parts = date.split("-")
        val months = listOf("","enero","febrero","marzo","abril","mayo","junio",
            "julio","agosto","septiembre","octubre","noviembre","diciembre")
        val day = parts[2].toIntOrNull() ?: 0
        val month = parts[1].toIntOrNull() ?: 0
        "$day de ${months.getOrElse(month) { "" }}"
    }
}
