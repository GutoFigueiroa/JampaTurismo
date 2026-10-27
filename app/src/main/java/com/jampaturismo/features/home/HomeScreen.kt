package com.jampaturismo.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.DeepNavy
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.core.ui.components.SafeWindowBadge
import com.jampaturismo.data.model.TideData
import com.jampaturismo.data.model.TouristPlace
import com.jampaturismo.data.model.WeatherData

/**
 * Responsável: Fernando Augusto Lustoza Figueirôa (Líder)
 * Tela Inicial - Roteiro Pré-Programado e Dashboard Inteligente
 */
@Composable
fun HomeScreen(
    tideData: TideData = TideData(),
    weatherData: WeatherData = WeatherData(),
    recommendedPlaces: List<TouristPlace> = emptyList(),
    onNavigateToTides: () -> Unit = {},
    onNavigateToPlaces: () -> Unit = {},
    onSelectPlace: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            HeaderSection()
        }

        // Hero Card: Painel Inteligente de Janela Segura
        item {
            HeroRecommendationCard(
                tideData = tideData,
                weatherData = weatherData,
                onExploreClick = onNavigateToPlaces
            )
        }

        // Mini Cards de Monitoramento Rápido
        item {
            QuickMetricsRow(
                tideData = tideData,
                weatherData = weatherData,
                onTideClick = onNavigateToTides
            )
        }

        // Seção Roteiros Recomendados para Hoje
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Passeios Recomendados Agora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onNavigateToPlaces) {
                    Text("Ver todos")
                }
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recommendedPlaces) { place ->
                    RecommendedPlaceCard(
                        place = place,
                        onClick = { onSelectPlace(place.id) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HeaderSection() {
    Column {
        Text(
            text = "JAMPA TURISMO",
            style = MaterialTheme.typography.labelLarge,
            color = AquaCyan
        )
        Text(
            text = "João Pessoa & Litoral",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun HeroRecommendationCard(
    tideData: TideData,
    weatherData: WeatherData,
    onExploreClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepNavy, OceanBlue)
                    )
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SafeWindowBadge(safetyLevel = tideData.currentSafetyLevel)

                Text(
                    text = "Dia Perfeito para Areia Vermelha!",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "A maré baixa atinge ${tideData.nextLowTide.height}m às ${tideData.nextLowTide.time}. " +
                            "Clima com ${weatherData.temperature}°C e mar calmo. ${tideData.safeWindowRemaining}.",
                    color = Color(0xFFD6E4FF),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = onExploreClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AquaCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Ver Roteiro do Dia",
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = DeepNavy
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickMetricsRow(
    tideData: TideData,
    weatherData: WeatherData,
    onTideClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Card Maré
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onTideClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = null,
                        tint = OceanBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Maré Atual",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${tideData.currentHeight}m",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanBlue
                )
                Text(
                    text = "Mín: ${tideData.nextLowTide.height}m às ${tideData.nextLowTide.time}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Card Tempo
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tempo em JP",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${weatherData.temperature}°C",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Vento: ${weatherData.windSpeedKmH} km/h",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RecommendedPlaceCard(
    place: TouristPlace,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = place.category.label,
                fontSize = 11.sp,
                color = AquaCyan,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = place.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = place.subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (place.tideDependency) {
                Text(
                    text = "🌊 Maré ideal: ≤ ${place.idealTideMaxHeight}m",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = OceanBlue
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    com.jampaturismo.core.theme.JampaTurismoTheme {
        HomeScreen(
            recommendedPlaces = com.jampaturismo.data.repository.TouristPlaceRepository().getPlaceById("areia_vermelha")?.let { listOf(it) } ?: emptyList()
        )
    }
}

