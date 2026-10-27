package com.jampaturismo.features.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.DeepNavy
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.data.model.TouristPlace

/**
 * Responsável: Matheus Henrique dos Santos Silva
 * Aba Mapa - Mapa Interativo com Google Maps SDK e GPS
 */
@Composable
fun MapScreen(
    places: List<TouristPlace> = emptyList(),
    onPlaceSelected: (TouristPlace) -> Unit = {}
) {
    // Coordenadas Centrais de João Pessoa / Orla de Tambaú
    val joaoPessoaCenter = remember { LatLng(-7.1150, -34.8250) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(joaoPessoaCenter, 11.5f)
    }

    var selectedPlace by remember { mutableStateOf<TouristPlace?>(places.firstOrNull()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Mapa Google
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = false // Ativar após solicitar permissões de ACCESS_FINE_LOCATION
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = true,
                myLocationButtonEnabled = false
            )
        ) {
            places.forEach { place ->
                Marker(
                    state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                    title = place.name,
                    snippet = place.subtitle,
                    onClick = {
                        selectedPlace = place
                        onPlaceSelected(place)
                        false
                    }
                )
            }
        }

        // Header Superior Flutuante
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "MAPA INTERATIVO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaCyan
                    )
                    Text(
                        text = "${places.size} Atrativos no Litoral",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = OceanBlue
                )
            }
        }

        // Card Inferior do Ponto Selecionado
        selectedPlace?.let { place ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = place.category.label,
                                fontSize = 11.sp,
                                color = AquaCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = place.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = place.subtitle,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { /* Abrir rota no Google Maps */ },
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Directions,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Rotas")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = place.travelTip,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
