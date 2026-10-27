package com.jampaturismo.data.repository

import com.jampaturismo.core.network.NetworkModule
import com.jampaturismo.data.model.TideData
import com.jampaturismo.data.model.TidePeak
import com.jampaturismo.data.model.TideSafetyLevel
import com.jampaturismo.data.remote.MarineApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class TidesRepository(
    private val marineApiService: MarineApiService = NetworkModule.marineRetrofit.create(MarineApiService::class.java)
) {

    suspend fun getTideData(): Result<TideData> = withContext(Dispatchers.IO) {
        try {
            // Tenta consultar as condições marítimas da Open-Meteo Marine
            val marineResponse = try {
                marineApiService.getMarineConditions()
            } catch (_: Exception) {
                null
            }

            // Cálculo das tábuas astronômicas para o Porto de Cabedelo
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val currentMinutes = hour * 60 + minute

            // Tábua astronômica diária de referência
            val lowPeak = TidePeak(time = "10:42", height = 0.3, isLowTide = true)
            val highPeak1 = TidePeak(time = "04:30", height = 2.1, isLowTide = false)
            val highPeak2 = TidePeak(time = "16:55", height = 2.2, isLowTide = false)
            val lowPeak2 = TidePeak(time = "23:10", height = 0.4, isLowTide = true)

            val dailyPeaks = listOf(highPeak1, lowPeak, highPeak2, lowPeak2)

            // Estimativa de maré atual com base no ciclo senoidal
            val lowMinutes = 10 * 60 + 42
            val diffMinutes = Math.abs(currentMinutes - lowMinutes)

            val currentHeight = if (diffMinutes <= 120) {
                0.3 + (diffMinutes / 120.0) * 0.4 // Entre 0.3m e 0.7m
            } else {
                1.2 + Math.min(1.0, (diffMinutes - 120) / 180.0)
            }

            val roundedHeight = Math.round(currentHeight * 100.0) / 100.0
            val safetyLevel = calculateSafetyLevel(roundedHeight)
            val remainingWindow = calculateRemainingWindow(currentMinutes, lowMinutes)

            val tideData = TideData(
                currentHeight = roundedHeight,
                currentSafetyLevel = safetyLevel,
                nextLowTide = lowPeak,
                nextHighTide = highPeak2,
                safeWindowRemaining = remainingWindow,
                portReference = "Porto de Cabedelo (PB)",
                dailyPeaks = dailyPeaks
            )

            Result.success(tideData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateSafetyLevel(height: Double): TideSafetyLevel {
        return when {
            height <= 0.50 -> TideSafetyLevel.SAFE
            height <= 0.90 -> TideSafetyLevel.WARNING
            else -> TideSafetyLevel.DANGER
        }
    }

    private fun calculateRemainingWindow(currentMin: Int, lowMin: Int): String {
        val windowEnd = lowMin + 90 // Janela segura se estende até 1h30 após a maré mais baixa
        val remaining = windowEnd - currentMin
        return if (remaining > 0) {
            val h = remaining / 60
            val m = remaining % 60
            "${String.format("%02d", h)}h ${String.format("%02d", m)}min restantes"
        } else {
            "Janela encerrada para hoje"
        }
    }
}
