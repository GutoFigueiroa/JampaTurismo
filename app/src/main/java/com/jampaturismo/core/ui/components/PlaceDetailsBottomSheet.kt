package com.jampaturismo.core.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.NavyBlueSecondary
import com.jampaturismo.core.theme.tideColors
import com.jampaturismo.data.model.TideSafetyLevel
import com.jampaturismo.data.model.TouristPlace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsBottomSheet(
    place: TouristPlace?,
    currentTideHeight: Double,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onDismissRequest: () -> Unit,
    onNavigateToMap: (TouristPlace) -> Unit
) {
    if (place == null) return

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Imagem do Atrativo
            Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                if (place.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = place.imageUrl,
                        contentDescription = place.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(NavyBlueSecondary)
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botão Favoritar
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favoritar",
                            tint = if (isFavorite) com.jampaturismo.core.theme.CoralDanger else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }


                    // Botão Fechar
                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar")
                    }
                }
            }


            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = place.category.label.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AquaCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = place.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card de Condição da Maré para este atrativo
                if (place.tideDependency && place.idealTideMaxHeight != null) {
                    val isTideFavorable = currentTideHeight <= place.idealTideMaxHeight
                    val badgeColor = if (isTideFavorable) MaterialTheme.tideColors.safe else MaterialTheme.tideColors.danger

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = badgeColor.copy(alpha = 0.12f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Waves,
                                contentDescription = null,
                                tint = badgeColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isTideFavorable) "Maré Favorável Agora!" else "Maré Inadequada para Travessia",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = badgeColor
                                )
                                Text(
                                    text = "Maré atual: ${currentTideHeight}m • Exigida: ≤ ${place.idealTideMaxHeight}m",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Descrição
                Text(
                    text = "Sobre o Passeio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = place.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dica Importante
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NavyBlueSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = place.travelTip,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Badge de Balneabilidade da SUDEMA (Inovação - Ponto 3)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (place.waterQuality.isSafe) com.jampaturismo.core.theme.TurquoiseSafe.copy(alpha = 0.12f)
                            else com.jampaturismo.core.theme.CoralDanger.copy(alpha = 0.12f)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💧", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Boletim de Balneabilidade (SUDEMA)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = place.waterQuality.label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (place.waterQuality.isSafe) com.jampaturismo.core.theme.TurquoiseSafe else com.jampaturismo.core.theme.CoralDanger
                        )
                    }
                }

                // Botão de Contato com Barqueiros / WhatsApp (Inovação Comercial - Ponto 3)
                if (!place.whatsappContact.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            val msg = "Olá! Gostaria de consultar saídas de catamarã para ${place.name} hoje."
                            val encodedMsg = Uri.encode(msg)
                            val waUri = Uri.parse("https://api.whatsapp.com/send?phone=${place.whatsappContact}&text=$encodedMsg")
                            val waIntent = Intent(Intent.ACTION_VIEW, waUri)
                            context.startActivity(waIntent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // Verde WhatsApp oficial
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "💬", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reservar no WhatsApp (${place.operatorName ?: "Operador Náutico"})",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Ações: Ver no Mapa e Traçar Rota
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDismissRequest()
                            onNavigateToMap(place)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ver no Mapa")
                    }

                    Button(
                        onClick = {
                            // Abre a navegação direta pelo app do Google Maps
                            val gmmIntentUri = Uri.parse("google.navigation:q=${place.latitude},${place.longitude}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                val browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${place.latitude},${place.longitude}")
                                context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyBlueSecondary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Traçar Rota")
                    }
                }
            }
        }
    }
}

