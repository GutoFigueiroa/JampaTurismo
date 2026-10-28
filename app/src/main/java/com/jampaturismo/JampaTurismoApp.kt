package com.jampaturismo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jampaturismo.core.navigation.Screen
import com.jampaturismo.core.navigation.bottomNavItems
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.features.home.HomeScreen
import com.jampaturismo.features.home.HomeViewModel
import com.jampaturismo.features.map.MapScreen
import com.jampaturismo.features.map.MapViewModel
import com.jampaturismo.features.places.PlacesScreen
import com.jampaturismo.features.places.PlacesViewModel
import com.jampaturismo.features.tides.TidesScreen
import com.jampaturismo.features.tides.TidesViewModel
import com.jampaturismo.features.weather.WeatherScreen
import com.jampaturismo.features.weather.WeatherViewModel

@Composable
fun JampaTurismoApp(
    homeViewModel: HomeViewModel = viewModel(),
    tidesViewModel: TidesViewModel = viewModel(),
    weatherViewModel: WeatherViewModel = viewModel(),
    placesViewModel: PlacesViewModel = viewModel(),
    mapViewModel: MapViewModel = viewModel()
) {
    val navController = rememberNavController()

    val homeUiState by homeViewModel.uiState.collectAsState()
    val tidesUiState by tidesViewModel.uiState.collectAsState()
    val weatherUiState by weatherViewModel.uiState.collectAsState()
    val placesUiState by placesViewModel.uiState.collectAsState()
    val mapUiState by mapViewModel.uiState.collectAsState()

    var selectedPlaceForDetails by remember { mutableStateOf<com.jampaturismo.data.model.TouristPlace?>(null) }

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
                    tideData = homeUiState.tideData,
                    weatherData = homeUiState.weatherData,
                    recommendedPlaces = homeUiState.recommendedPlaces,
                    onNavigateToTides = { navController.navigate(Screen.Tides.route) },
                    onNavigateToPlaces = { navController.navigate(Screen.Places.route) },
                    onSelectPlace = { selectedPlaceForDetails = it }
                )
            }

            // 2. Aba Marés (Matheus Henrique do Egíto)
            composable(Screen.Tides.route) {
                TidesScreen(tideData = tidesUiState.tideData)
            }

            // 3. Aba Explorar (Murillo Lopes)
            composable(Screen.Places.route) {
                val context = androidx.compose.ui.platform.LocalContext.current
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    placesViewModel.attachContext(context)
                }

                PlacesScreen(
                    places = placesUiState.filteredPlaces,
                    selectedCategory = placesUiState.selectedCategory,
                    searchQuery = placesUiState.searchQuery,
                    favoriteIds = placesUiState.favoriteIds,
                    showOnlyFavorites = placesUiState.showOnlyFavorites,
                    onSearchQueryChange = { placesViewModel.onSearchQueryChange(it) },
                    onCategorySelected = { placesViewModel.onSelectCategory(it) },
                    onToggleShowOnlyFavorites = { placesViewModel.toggleShowOnlyFavorites() },
                    onToggleFavorite = { placesViewModel.toggleFavorite(it) },
                    onSelectPlace = { placeId ->
                        selectedPlaceForDetails = placesUiState.places.find { it.id == placeId }
                    }
                )
            }

            // 4. Aba Mapa (Matheus Henrique dos Santos Silva)
            composable(Screen.Map.route) {
                MapScreen(
                    places = mapUiState.places,
                    onPlaceSelected = { mapViewModel.onSelectPlace(it) }
                )
            }
        }

        // BottomSheet Interativo de Detalhes do Passeio (Sprint 4 & 5)
        if (selectedPlaceForDetails != null) {
            val place = selectedPlaceForDetails!!
            val isFavorite = placesUiState.favoriteIds.contains(place.id)

            com.jampaturismo.core.ui.components.PlaceDetailsBottomSheet(
                place = place,
                currentTideHeight = homeUiState.tideData.currentHeight,
                isFavorite = isFavorite,
                onToggleFavorite = { placesViewModel.toggleFavorite(place.id) },
                onDismissRequest = { selectedPlaceForDetails = null },
                onNavigateToMap = { selectedPlace ->
                    mapViewModel.onSelectPlace(selectedPlace)
                    navController.navigate(Screen.Map.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

    }
}



@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun JampaTurismoAppPreview() {
    com.jampaturismo.core.theme.JampaTurismoTheme {
        JampaTurismoApp()
    }
}

