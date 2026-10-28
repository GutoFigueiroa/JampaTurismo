package com.jampaturismo.features.places

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jampaturismo.core.storage.FavoritesPreferences
import com.jampaturismo.data.model.PlaceCategory
import com.jampaturismo.data.model.TouristPlace
import com.jampaturismo.data.repository.TouristPlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlacesUiState(
    val places: List<TouristPlace> = emptyList(),
    val filteredPlaces: List<TouristPlace> = emptyList(),
    val selectedCategory: PlaceCategory? = null,
    val searchQuery: String = "",
    val favoriteIds: Set<String> = emptySet(),
    val showOnlyFavorites: Boolean = false
)

class PlacesViewModel(
    private val repository: TouristPlaceRepository = TouristPlaceRepository()
) : ViewModel() {

    private var favoritesPrefs: FavoritesPreferences? = null

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState.asStateFlow()

    init {
        loadPlaces()
    }

    fun attachContext(context: Context) {
        if (favoritesPrefs == null) {
            favoritesPrefs = FavoritesPreferences(context.applicationContext)
            val persistedFavorites = favoritesPrefs?.getFavoriteIds() ?: emptySet()
            _uiState.value = _uiState.value.copy(
                favoriteIds = persistedFavorites,
                filteredPlaces = filterList(
                    _uiState.value.places,
                    _uiState.value.selectedCategory,
                    _uiState.value.searchQuery,
                    _uiState.value.showOnlyFavorites,
                    persistedFavorites
                )
            )
        }
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            repository.places.collect { list ->
                _uiState.value = _uiState.value.copy(
                    places = list,
                    filteredPlaces = filterList(
                        list,
                        _uiState.value.selectedCategory,
                        _uiState.value.searchQuery,
                        _uiState.value.showOnlyFavorites,
                        _uiState.value.favoriteIds
                    )
                )
            }
        }
    }

    fun onSelectCategory(category: PlaceCategory?) {
        val current = _uiState.value
        _uiState.value = current.copy(
            selectedCategory = category,
            filteredPlaces = filterList(current.places, category, current.searchQuery, current.showOnlyFavorites, current.favoriteIds)
        )
    }

    fun onSearchQueryChange(query: String) {
        val current = _uiState.value
        _uiState.value = current.copy(
            searchQuery = query,
            filteredPlaces = filterList(current.places, current.selectedCategory, query, current.showOnlyFavorites, current.favoriteIds)
        )
    }

    fun toggleShowOnlyFavorites() {
        val current = _uiState.value
        val newShowOnly = !current.showOnlyFavorites
        _uiState.value = current.copy(
            showOnlyFavorites = newShowOnly,
            filteredPlaces = filterList(current.places, current.selectedCategory, current.searchQuery, newShowOnly, current.favoriteIds)
        )
    }

    fun toggleFavorite(placeId: String) {
        val current = _uiState.value
        val updatedFavorites = current.favoriteIds.toMutableSet()
        if (updatedFavorites.contains(placeId)) {
            updatedFavorites.remove(placeId)
        } else {
            updatedFavorites.add(placeId)
        }
        favoritesPrefs?.toggleFavorite(placeId)

        _uiState.value = current.copy(
            favoriteIds = updatedFavorites,
            filteredPlaces = filterList(current.places, current.selectedCategory, current.searchQuery, current.showOnlyFavorites, updatedFavorites)
        )
    }

    private fun filterList(
        list: List<TouristPlace>,
        category: PlaceCategory?,
        query: String,
        onlyFavorites: Boolean,
        favorites: Set<String>
    ): List<TouristPlace> {
        return list.filter { place ->
            val matchesCategory = category == null || place.category == category
            val matchesQuery = query.isBlank() ||
                    place.name.contains(query, ignoreCase = true) ||
                    place.subtitle.contains(query, ignoreCase = true) ||
                    place.description.contains(query, ignoreCase = true)
            val matchesFavorite = !onlyFavorites || favorites.contains(place.id)

            matchesCategory && matchesQuery && matchesFavorite
        }
    }
}

