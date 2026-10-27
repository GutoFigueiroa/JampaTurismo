package com.jampaturismo.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// DTOs da API Open-Meteo Weather
data class OpenMeteoWeatherResponse(
    @SerializedName("current")
    val current: CurrentWeatherDto?,
    @SerializedName("daily")
    val daily: DailyWeatherDto?
)

data class CurrentWeatherDto(
    @SerializedName("time")
    val time: String?,
    @SerializedName("temperature_2m")
    val temperature: Double?,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double?,
    @SerializedName("relative_humidity_2m")
    val humidity: Int?,
    @SerializedName("precipitation_probability")
    val precipitationProbability: Int?,
    @SerializedName("weather_code")
    val weatherCode: Int?,
    @SerializedName("wind_speed_10m")
    val windSpeed: Double?
)

data class DailyWeatherDto(
    @SerializedName("uv_index_max")
    val uvIndexMax: List<Double>?,
    @SerializedName("sunset")
    val sunset: List<String>?
)

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double = -7.1195,
        @Query("longitude") longitude: Double = -34.8450,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation_probability,weather_code,wind_speed_10m",
        @Query("daily") daily: String = "uv_index_max,sunset",
        @Query("timezone") timezone: String = "America/Fortaleza"
    ): OpenMeteoWeatherResponse
}
