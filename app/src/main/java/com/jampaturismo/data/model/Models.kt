package com.jampaturismo.data.model

enum class TideSafetyLevel {
    SAFE,       // Janela Segura (< 0.5m)
    WARNING,    // Atenção (0.5m - 0.9m)
    DANGER      // Perigo / Maré Alta (> 0.9m)
}

data class TidePeak(
    val time: String,      // ex: "10:45"
    val height: Double,    // ex: 0.3
    val isLowTide: Boolean // true para maré baixa, false para alta
)

data class TideData(
    val currentHeight: Double = 0.35,
    val currentSafetyLevel: TideSafetyLevel = TideSafetyLevel.SAFE,
    val nextLowTide: TidePeak = TidePeak("10:42", 0.3, isLowTide = true),
    val nextHighTide: TidePeak = TidePeak("16:55", 2.2, isLowTide = false),
    val safeWindowRemaining: String = "01h 45min restantes",
    val portReference: String = "Porto de Cabedelo (PB)",
    val dailyPeaks: List<TidePeak> = listOf(
        TidePeak("04:30", 2.1, false),
        TidePeak("10:42", 0.3, true),
        TidePeak("16:55", 2.2, false),
        TidePeak("23:10", 0.4, true)
    )
)

data class WeatherData(
    val temperature: Int = 29,
    val feelsLike: Int = 32,
    val condition: String = "Ensolarado com poucas nuvens",
    val uvIndex: Int = 9,
    val windSpeedKmH: Int = 18,
    val humidityPercent: Int = 72,
    val rainProbabilityPercent: Int = 10,
    val sunsetTime: String = "17:28"
)

enum class PlaceCategory(val label: String) {
    NATURAL_POOLS("Piscinas Naturais"),
    BEACH("Praias"),
    SUNSET("Pôr do Sol"),
    CULTURE("Histórico / Cultura")
}

enum class WaterQualityStatus(val label: String, val isSafe: Boolean) {
    EXCELLENT("Própria para Banho (Excelente)", true),
    SAFE("Própria para Banho", true),
    IMPROPER("Imprópria para Banho (Aviso SUDEMA)", false)
}

data class TouristPlace(
    val id: String,
    val name: String,
    val subtitle: String,
    val category: PlaceCategory,
    val description: String,
    val tideDependency: Boolean,
    val idealTideMaxHeight: Double?, // ex: 0.5m para Areia Vermelha
    val latitude: Double,
    val longitude: Double,
    val travelTip: String,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val whatsappContact: String? = null,    // ex: "5583999999999"
    val operatorName: String? = null,       // ex: "Associação de Barqueiros de Camboinha"
    val waterQuality: WaterQualityStatus = WaterQualityStatus.EXCELLENT
)


