package com.jampaturismo.features.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jampaturismo.data.model.TouristPlace
import com.jampaturismo.data.repository.TouristPlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class MapUiState(
    val places: List<TouristPlace> = emptyList(),
    val selectedPlace: TouristPlace? = null,
    val userLocation: LatLng? = null,
    val hasLocationPermission: Boolean = false,
    val distanceToSelectedKm: Double? = null
)

class MapViewModel(
    private val repository: TouristPlaceRepository = TouristPlaceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            repository.places.collect { list ->
                _uiState.value = _uiState.value.copy(
                    places = list,
                    selectedPlace = list.firstOrNull()
                )
                calculateDistance()
            }
        }
    }

    fun onSelectPlace(place: TouristPlace) {
        _uiState.value = _uiState.value.copy(selectedPlace = place)
        calculateDistance()
    }

    fun onLocationUpdated(lat: Double, lng: Double) {
        _uiState.value = _uiState.value.copy(
            userLocation = LatLng(lat, lng),
            hasLocationPermission = true
        )
        calculateDistance()
    }

    private fun calculateDistance() {
        val user = _uiState.value.userLocation ?: return
        val target = _uiState.value.selectedPlace ?: return

        val userLoc = Location("User").apply {
            latitude = user.latitude
            longitude = user.longitude
        }
        val targetLoc = Location("Target").apply {
            latitude = target.latitude
            longitude = target.longitude
        }

        val distanceMeters = userLoc.distanceTo(targetLoc)
        val distanceKm = (distanceMeters / 100.0).roundToInt() / 10.0 // ex: 8.4 km
        _uiState.value = _uiState.value.copy(distanceToSelectedKm = distanceKm)
    }
}
