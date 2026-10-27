package com.jampaturismo.features.tides

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jampaturismo.data.model.TideData
import com.jampaturismo.data.repository.TidesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TidesUiState(
    val isLoading: Boolean = false,
    val tideData: TideData = TideData(),
    val errorMessage: String? = null
)

class TidesViewModel(
    private val tidesRepository: TidesRepository = TidesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TidesUiState())
    val uiState: StateFlow<TidesUiState> = _uiState.asStateFlow()

    init {
        refreshTides()
    }

    fun refreshTides() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = tidesRepository.getTideData()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tideData = result.getOrThrow()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Usando dados locais de maré."
                )
            }
        }
    }
}
