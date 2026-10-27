package com.jampaturismo.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// DTOs da API Open-Meteo Marine
data class OpenMeteoMarineResponse(
    @SerializedName("hourly")
    val hourly: HourlyMarineDto?
)

data class HourlyMarineDto(
    @SerializedName("time")
    val time: List<String>?,
    @SerializedName("wave_height")
    val waveHeight: List<Double>?,
    @SerializedName("wave_period")
    val wavePeriod: List<Double>?
)

interface MarineApiService {
    @GET("v1/marine")
    suspend fun getMarineConditions(
        @Query("latitude") latitude: Double = -6.9800,   // Porto de Cabedelo
        @Query("longitude") longitude: Double = -34.8333,
        @Query("hourly") hourly: String = "wave_height,wave_period",
        @Query("timezone") timezone: String = "America/Fortaleza"
    ): OpenMeteoMarineResponse
}
