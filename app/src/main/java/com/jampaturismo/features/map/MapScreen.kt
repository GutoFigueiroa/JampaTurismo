package com.jampaturismo.features.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.data.model.TouristPlace
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Responsável: Matheus Henrique dos Santos Silva
 * Aba Mapa - Mapa Interativo com Google Maps SDK e GPS
 */
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    places: List<TouristPlace> = emptyList(),
    userLocation: LatLng? = null,
    onPlaceSelected: (TouristPlace) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Coordenadas Centrais de João Pessoa / Orla de Tambaú
    val joaoPessoaCenter = remember { LatLng(-7.1150, -34.8250) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(joaoPessoaCenter, 11.5f)
    }

    var selectedPlace by remember { mutableStateOf<TouristPlace?>(places.firstOrNull()) }
    var currentUserLatLng by remember { mutableStateOf<LatLng?>(userLocation) }

    // Atualiza o local selecionado quando os dados carregarem
    LaunchedEffect(places) {
        if (selectedPlace == null && places.isNotEmpty()) {
            selectedPlace = places.first()
        }
    }

    // Permissão de Localização (GPS)
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    fun fetchUserLocation() {
        if (hasLocationPermission) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    val latLng = LatLng(loc.latitude, loc.longitude)
                    currentUserLatLng = latLng
                }
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            fetchUserLocation()
        }
    }

    LaunchedEffect(Unit) {
        if (hasLocationPermission) {
            fetchUserLocation()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Cálculo da distância em km até o atrativo selecionado
    val distanceKm = remember(currentUserLatLng, selectedPlace) {
        val user = currentUserLatLng
        val target = selectedPlace
        if (user != null && target != null) {
            val userLoc = Location("User").apply {
                latitude = user.latitude
                longitude = user.longitude
            }
            val targetLoc = Location("Target").apply {
                latitude = target.latitude
                longitude = target.longitude
            }
            val meters = userLoc.distanceTo(targetLoc)
            (meters / 100.0).roundToInt() / 10.0 // em km com 1 casa decimal
        } else {
            null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Mapa Google
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = true,
                myLocationButtonEnabled = false, // Usamos nosso botão customizado
                mapToolbarEnabled = true
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
                        text = "${places.size} Atrativos no Litoral da Paraíba",
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

        // Botões de Ação Flutuantes (GPS e Panorâmica)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Botão: Centralizar no Usuário (GPS)
            FloatingActionButton(
                onClick = {
                    if (hasLocationPermission) {
                        fetchUserLocation()
                        currentUserLatLng?.let { userPos ->
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(userPos, 14f)
                                )
                            }
                        }
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = OceanBlue,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Minha Localização GPS"
                )
            }

            // Botão: Visão Geral de João Pessoa
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(joaoPessoaCenter, 11.5f)
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = OceanBlue,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = "Ver Orla de JP"
                )
            }
        }

        // Card Inferior do Ponto Selecionado com Distância em KM
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

                            // Indicador de Distância GPS
                            if (distanceKm != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "📍 Você está a ${distanceKm} km daqui",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanBlue
                                )
                            }
                        }

                        Button(
                            onClick = {
                                val gmmIntentUri = Uri.parse("google.navigation:q=${place.latitude},${place.longitude}&mode=d")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                    setPackage("com.google.android.apps.maps")
                                }
                                try {
                                    context.startActivity(mapIntent)
                                } catch (e: Exception) {
                                    val webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${place.latitude},${place.longitude}")
                                    context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
                                }
                            },
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

