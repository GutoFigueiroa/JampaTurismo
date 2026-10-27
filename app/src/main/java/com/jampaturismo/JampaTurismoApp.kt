package com.jampaturismo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jampaturismo.core.navigation.Screen
import com.jampaturismo.core.navigation.bottomNavItems
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.data.model.TideData
import com.jampaturismo.data.model.WeatherData
import com.jampaturismo.data.repository.TouristPlaceRepository
import com.jampaturismo.features.home.HomeScreen
import com.jampaturismo.features.map.MapScreen
import com.jampaturismo.features.places.PlacesScreen
import com.jampaturismo.features.tides.TidesScreen
import com.jampaturismo.features.weather.WeatherScreen

@Composable
fun JampaTurismoApp() {
    val navController = rememberNavController()
    val repository = remember { TouristPlaceRepository() }
    val places by repository.places.collectAsState(initial = emptyList())
    val tideData = remember { TideData() }
    val weatherData = remember { WeatherData() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = NavigationBarDefaults.Elevation
            ) {
                bottomNavItems.forEach { screen ->
                    val isSelected = currentRoute == screen.route

                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = { Text(text = screen.title) },
                        selected = isSelected,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OceanBlue,
                            selectedTextColor = OceanBlue,
                            indicatorColor = AquaCyan.copy(alpha = 0.25f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Aba Início (Fernando - Líder)
            composable(Screen.Home.route) {
                HomeScreen(
                    tideData = tideData,
                    weatherData = weatherData,
                    recommendedPlaces = places,
                    onNavigateToTides = { navController.navigate(Screen.Tides.route) },
                    onNavigateToPlaces = { navController.navigate(Screen.Places.route) },
                    onSelectPlace = { /* Detalhes do local */ }
                )
            }

            // 2. Aba Marés (Matheus Henrique do Egíto)
            composable(Screen.Tides.route) {
                TidesScreen(tideData = tideData)
            }

            // 3. Aba Explorar (Murillo Lopes)
            composable(Screen.Places.route) {
                PlacesScreen(
                    places = places,
                    onSelectPlace = { /* Detalhes */ }
                )
            }

            // 4. Aba Mapa (Matheus Henrique dos Santos Silva)
            composable(Screen.Map.route) {
                MapScreen(places = places)
            }
        }
    }
}
