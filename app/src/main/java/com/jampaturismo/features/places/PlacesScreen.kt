package com.jampaturismo.features.places

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.CoralDanger
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.data.model.PlaceCategory
import com.jampaturismo.data.model.TouristPlace

/**
 * Responsável: Murillo Lopes
 * Aba Explorar - Roteiro de Viagens e Passeios
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    places: List<TouristPlace> = emptyList(),
    selectedCategory: PlaceCategory? = null,
    searchQuery: String = "",
    favoriteIds: Set<String> = emptySet(),
    showOnlyFavorites: Boolean = false,
    onSearchQueryChange: (String) -> Unit = {},
    onCategorySelected: (PlaceCategory?) -> Unit = {},
    onToggleShowOnlyFavorites: () -> Unit = {},
    onToggleFavorite: (String) -> Unit = {},
    onSelectPlace: (String) -> Unit = {}
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

        // 1. Barra de Busca Textual (Melhoria Imediata)
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar praias, falésias, corais...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = OceanBlue
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpar busca"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AquaCyan,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }

        // 2. Filtro por Categorias + Chip de Favoritos
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Chip Especial de Favoritos
                item {
                    FilterChip(
                        selected = showOnlyFavorites,
                        onClick = onToggleShowOnlyFavorites,
                        label = { Text("❤️ Favoritos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CoralDanger.copy(alpha = 0.2f),
                            selectedLabelColor = CoralDanger
                        )
                    )
                }

                item {
                    FilterChip(
                        selected = selectedCategory == null && !showOnlyFavorites,
                        onClick = { onCategorySelected(null) },
                        label = { Text("Todos") }
                    )
                }
                items(PlaceCategory.values()) { category ->
                    FilterChip(
                        selected = selectedCategory == category && !showOnlyFavorites,
                        onClick = { onCategorySelected(category) },
                        label = { Text(category.label) }
                    )
                }
            }
        }

        // Empty State caso nada seja encontrado
        if (places.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏝️ Nenhum destino encontrado",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (showOnlyFavorites) "Você ainda não favoritou nenhum passeio." else "Tente buscar por outro termo.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Cards dos Atrativos com Fotos e Botão de Favorito
        items(places) { place ->
            val isFavorite = favoriteIds.contains(place.id)
            PlaceCard(
                place = place,
                isFavorite = isFavorite,
                onToggleFavorite = { onToggleFavorite(place.id) },
                onClick = { onSelectPlace(place.id) }
            )
        }
    }
}

@Composable
private fun PlaceCard(
    place: TouristPlace,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
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
        Column {
            // Imagem do Atrativo com Botão de Favoritar Sobreposto
            Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                if (place.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = place.imageUrl,
                        contentDescription = place.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Botão de Favoritar redondo no topo direito
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Desfavoritar" else "Favoritar",
                        tint = if (isFavorite) CoralDanger else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            Column(modifier = Modifier.padding(16.dp)) {
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
                                    "Maré ≤ ${place.idealTideMaxHeight}m",
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

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = place.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}


@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PlacesScreenPreview() {
    val samplePlaces = com.jampaturismo.data.repository.TouristPlaceRepository().getPlaceById("areia_vermelha")?.let { listOf(it) } ?: emptyList()
    com.jampaturismo.core.theme.JampaTurismoTheme {
        PlacesScreen(places = samplePlaces)
    }
}

