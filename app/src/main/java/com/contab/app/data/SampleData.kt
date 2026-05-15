package com.contab.app.data

val SAMPLE_TRANSACTIONS = listOf(
    Transaction(1,  "Restaurante La Candelaria", 45000,  "food",          "2026-04-19", TransactionMethod.SCAN),
    Transaction(2,  "TransMilenio",              2800,   "transport",     "2026-04-19", TransactionMethod.VOICE),
    Transaction(3,  "Netflix",                   37900,  "entertainment", "2026-04-18", TransactionMethod.MANUAL),
    Transaction(4,  "Farmacia Cruz Verde",        28500,  "health",        "2026-04-18", TransactionMethod.SCAN),
    Transaction(5,  "Rappi Market",              89000,  "shopping",      "2026-04-17", TransactionMethod.SCAN),
    Transaction(6,  "Luz EPM",                   142000, "services",      "2026-04-16", TransactionMethod.MANUAL),
    Transaction(7,  "Café Pergamino",            18000,  "food",          "2026-04-15", TransactionMethod.SCAN),
    Transaction(8,  "Uber",                      22000,  "transport",     "2026-04-15", TransactionMethod.VOICE),
    Transaction(9,  "Curso Platzi",              59000,  "education",     "2026-04-14", TransactionMethod.MANUAL),
    Transaction(10, "Supermercado Éxito",        215000, "shopping",      "2026-04-13", TransactionMethod.SCAN),
)

val BUDGET = 1_200_000L

fun generateHeatData(): List<Int> {
    val days = 84
    return buildList {
        for (i in 0 until days) {
            val r  = Math.sin(i * 7.3 + 1.5) * 0.5 + 0.5
            val r2 = Math.sin(i * 2.1 + 3.7) * 0.5 + 0.5
            var v  = (r * r2 * 5).toInt()
            if (i % 11 == 0 || i % 17 == 0) v = 0
            add(minOf(v, 4))
        }
    }.toMutableList().also { it[it.size - 1] = 4 }
}
