package com.jampaturismo.data.repository

import com.jampaturismo.core.network.NetworkModule
import com.jampaturismo.data.model.WeatherData
import com.jampaturismo.data.remote.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class WeatherRepository(
    private val apiService: WeatherApiService = NetworkModule.weatherRetrofit.create(WeatherApiService::class.java)
) {

    suspend fun getWeatherData(): Result<WeatherData> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getForecast()
            val current = response.current

            val temp = current?.temperature?.roundToInt() ?: 29
            val feelsLike = current?.apparentTemperature?.roundToInt() ?: 32
            val humidity = current?.humidity ?: 70
            val rainProb = current?.precipitationProbability ?: 10
            val wind = current?.windSpeed?.roundToInt() ?: 18
            val weatherCode = current?.weatherCode ?: 0
            val uv = response.daily?.uvIndexMax?.firstOrNull()?.roundToInt() ?: 9
            val sunset = response.daily?.sunset?.firstOrNull()?.takeLast(5) ?: "17:28"

            val weatherData = WeatherData(
                temperature = temp,
                feelsLike = feelsLike,
                condition = mapWeatherCodeToDescription(weatherCode),
                uvIndex = uv,
                windSpeedKmH = wind,
                humidityPercent = humidity,
                rainProbabilityPercent = rainProb,
                sunsetTime = sunset
            )

            Result.success(weatherData)
        } catch (e: Exception) {
            // Fallback resiliente caso o usuário esteja sem sinal na praia
            Result.failure(e)
        }
    }

    private fun mapWeatherCodeToDescription(code: Int): String {
        return when (code) {
            0 -> "Céu Limpo e Ensolarado"
            1, 2 -> "Predomínio de Sol com Poucas Nuvens"
            3 -> "Nublado"
            45, 48 -> "Névoa Úmida"
            51, 53, 55 -> "Garoa / Chuva Fraca Passageira"
            61, 63, 65 -> "Chuva Moderada"
            80, 81, 82 -> "Pancadas de Chuva Tropicais"
            else -> "Ensolarado com Vento Litorâneo"
        }
    }
}
