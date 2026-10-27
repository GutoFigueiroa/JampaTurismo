package com.jampaturismo.features.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val favoriteIds: Set<String> = emptySet()
)

class PlacesViewModel(
    private val repository: TouristPlaceRepository = TouristPlaceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState.asStateFlow()

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            repository.places.collect { list ->
                _uiState.value = _uiState.value.copy(
                    places = list,
                    filteredPlaces = filterList(list, _uiState.value.selectedCategory, _uiState.value.searchQuery)
                )
            }
        }
    }

    fun onSelectCategory(category: PlaceCategory?) {
        val currentList = _uiState.value.places
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            filteredPlaces = filterList(currentList, category, _uiState.value.searchQuery)
        )
    }

    fun onSearchQueryChange(query: String) {
        val currentList = _uiState.value.places
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredPlaces = filterList(currentList, _uiState.value.selectedCategory, query)
        )
    }

    fun toggleFavorite(placeId: String) {
        val currentFavorites = _uiState.value.favoriteIds.toMutableSet()
        if (currentFavorites.contains(placeId)) {
            currentFavorites.remove(placeId)
        } else {
            currentFavorites.add(placeId)
        }
        _uiState.value = _uiState.value.copy(favoriteIds = currentFavorites)
    }

    private fun filterList(
        list: List<TouristPlace>,
        category: PlaceCategory?,
        query: String
    ): List<TouristPlace> {
        return list.filter { place ->
            val matchesCategory = category == null || place.category == category
            val matchesQuery = query.isBlank() ||
                    place.name.contains(query, ignoreCase = true) ||
                    place.subtitle.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }
}
