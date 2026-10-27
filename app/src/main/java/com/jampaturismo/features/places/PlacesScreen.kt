package com.jampaturismo.features.places

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.data.model.PlaceCategory
import com.jampaturismo.data.model.TouristPlace

/**
 * Responsável: Murillo Lopes
 * Aba Explorar - Roteiro de Viagens e Passeios
 */
@Composable
fun PlacesScreen(
    places: List<TouristPlace> = emptyList(),
    onSelectPlace: (String) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf<PlaceCategory?>(null) }

    val filteredPlaces = remember(selectedCategory, places) {
        if (selectedCategory == null) places
        else places.filter { it.category == selectedCategory }
    }

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
                    text = "EXPLORAR",
                    style = MaterialTheme.typography.labelLarge,
                    color = AquaCyan
                )
                Text(
                    text = "Roteiros & Passeios",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Destinos litorâneos mapeados e sincronizados com a maré",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Filtro por Categorias
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Todos") }
                    )
                }
                items(PlaceCategory.values()) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category.label) }
                    )
                }
            }
        }

        // Cards dos Atrativos
        items(filteredPlaces) { place ->
            PlaceCard(
                place = place,
                onClick = { onSelectPlace(place.id) }
            )
        }
    }
}

@Composable
private fun PlaceCard(
    place: TouristPlace,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = place.category.label.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AquaCyan
                )
                if (place.tideDependency) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                "Depende de Maré ≤ ${place.idealTideMaxHeight}m",
                                fontSize = 11.sp
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Waves,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = OceanBlue
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = place.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = place.subtitle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = place.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = OceanBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = place.travelTip,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
