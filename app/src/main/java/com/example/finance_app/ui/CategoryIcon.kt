package com.example.finance_app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

fun iconForCategory(catId: String): ImageVector = when (catId) {
    "food"          -> Icons.Outlined.Restaurant
    "transport"     -> Icons.Outlined.DirectionsBus
    "services"      -> Icons.Outlined.Bolt
    "health"        -> Icons.Outlined.Favorite
    "entertainment" -> Icons.Outlined.Movie
    "shopping"      -> Icons.Outlined.ShoppingBag
    "education"     -> Icons.Filled.MenuBook
    else            -> Icons.Outlined.Inventory2
}
