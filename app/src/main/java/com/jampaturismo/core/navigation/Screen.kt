package com.jampaturismo.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Início", Icons.Default.Home)
    object Tides : Screen("tides", "Marés", Icons.Default.CalendarMonth)
    object Places : Screen("places", "Explorar", Icons.Default.Explore)
    object Map : Screen("map", "Mapa", Icons.Default.Map)
    
    // Rota auxiliar de detalhes
    object PlaceDetails : Screen("places/{placeId}", "Detalhes", Icons.Default.Explore) {
        fun createRoute(placeId: String) = "places/$placeId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Tides,
    Screen.Places,
    Screen.Map
)
