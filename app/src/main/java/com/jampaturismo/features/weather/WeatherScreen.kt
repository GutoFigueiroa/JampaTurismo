package com.jampaturismo.features.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.DeepNavy
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.data.model.WeatherData

/**
 * Responsável: Esdras de Almeida Oliveira
 * Módulo de Clima / Tempo - Monitoramento Meteorológico para Turismo Litorâneo
 */
@Composable
fun WeatherScreen(
    weatherData: WeatherData = WeatherData()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "METEOROLOGIA",
                    style = MaterialTheme.typography.labelLarge,
                    color = AquaCyan
                )
                Text(
                    text = "Clima & Vento",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Previsão para João Pessoa e Litoral Paraibano",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Card Principal com Gradiente
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                colors = listOf(DeepNavy, OceanBlue)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Agora em João Pessoa",
                                    color = Color(0xFFD6E4FF),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${weatherData.temperature}°C",
                                    color = Color.White,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "Sensação de ${weatherData.feelsLike}°C",
                                    color = Color(0xFFD6E4FF),
                                    fontSize = 13.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = Color(0xFFFFB703),
                                modifier = Modifier.size(64.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = weatherData.condition,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Condições Náuticas e Balneabilidade",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Grade de Indicadores de Vento, UV e Chuva
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeatherMetricItem(
                    modifier = Modifier.weight(1f),
                    title = "Vento",
                    value = "${weatherData.windSpeedKmH} km/h",
                    subtitle = "Mar calmo a moderado",
                    icon = Icons.Default.Air
                )
                WeatherMetricItem(
                    modifier = Modifier.weight(1f),
                    title = "Índice UV",
                    value = "${weatherData.uvIndex} (Muito Alto)",
                    subtitle = "Protetor solar essencial",
                    icon = Icons.Default.WbSunny
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeatherMetricItem(
                    modifier = Modifier.weight(1f),
                    title = "Prob. Chuva",
                    value = "${weatherData.rainProbabilityPercent}%",
                    subtitle = "Baixo risco de chuvas",
                    icon = Icons.Default.Umbrella
                )
                WeatherMetricItem(
                    modifier = Modifier.weight(1f),
                    title = "Pôr do Sol",
                    value = weatherData.sunsetTime,
                    subtitle = "Píer do Jacaré",
                    icon = Icons.Default.WbTwilight
                )
            }
        }
    }
}

@Composable
private fun WeatherMetricItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OceanBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp
            )
        }
    }
}
