package com.jampaturismo.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jampaturismo.data.model.TideData
import com.jampaturismo.data.model.TouristPlace
import com.jampaturismo.data.model.WeatherData
import com.jampaturismo.data.repository.TidesRepository
import com.jampaturismo.data.repository.TouristPlaceRepository
import com.jampaturismo.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val tideData: TideData = TideData(),
    val weatherData: WeatherData = WeatherData(),
    val recommendedPlaces: List<TouristPlace> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(
    private val tidesRepository: TidesRepository = TidesRepository(),
    private val weatherRepository: WeatherRepository = WeatherRepository(),
    private val touristPlaceRepository: TouristPlaceRepository = TouristPlaceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val tideResult = tidesRepository.getTideData()
            val weatherResult = weatherRepository.getWeatherData()

            touristPlaceRepository.places.collect { placesList ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tideData = tideResult.getOrDefault(TideData()),
                    weatherData = weatherResult.getOrDefault(WeatherData()),
                    recommendedPlaces = placesList
                )
            }
        }
    }
}
