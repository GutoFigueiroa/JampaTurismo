package com.jampaturismo.core.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jampaturismo.core.theme.AquaCyan
import com.jampaturismo.core.theme.CoralDanger
import com.jampaturismo.core.theme.OceanBlue
import com.jampaturismo.core.theme.TurquoiseSafe
import com.jampaturismo.data.model.TidePeak
import java.util.Calendar

/**
 * Gráfico Interativo em Canvas da Curva da Maré (24h)
 * Traça a curva senoidal das marés astronômicas do Porto de Cabedelo
 * com linha d'água de segurança (0.5m) e indicador do momento atual.
 */
@Composable
fun TideWaveChart(
    dailyPeaks: List<TidePeak>,
    currentHeight: Double,
    modifier: Modifier = Modifier
) {
    // Animação suave de pulso no marcador do horário atual
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Curva da Maré em Tempo Real",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Ciclo das 24h no Porto de Cabedelo",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Legenda da Janela Segura
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(TurquoiseSafe, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Janela ≤ 0.5m",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TurquoiseSafe
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Canvas da Curva da Maré
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                val w = size.width
                val h = size.height
                val paddingBottom = 24f
                val effectiveH = h - paddingBottom

                // Eixo de segurança: linha de 0.5m (aproximadamente na altura inferior)
                // Cota de maré vai de 0.2m (base) a 2.5m (topo)
                val minTide = 0.0f
                val maxTide = 2.6f
                fun tideToY(tide: Float): Float {
                    val ratio = (tide - minTide) / (maxTide - minTide)
                    return effectiveH * (1f - ratio)
                }

                val safeLineY = tideToY(0.5f)

                // 1. Linha horizontal tracejada da Janela Segura (0.5m)
                drawLine(
                    color = TurquoiseSafe.copy(alpha = 0.5f),
                    start = Offset(0f, safeLineY),
                    end = Offset(w, safeLineY),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                )

                // 2. Traçado da Curva Senoidal Contínua da Maré (24h)
                val curvePath = Path()
                val fillPath = Path()

                val pointsCount = 60
                val points = mutableListOf<Offset>()

                for (i in 0..pointsCount) {
                    val progress = i.toFloat() / pointsCount.toFloat()
                    val hour = progress * 24f
                    // Simulação da curva semidiurna de Cabedelo (2 picos e 2 vales a cada 12.4h)
                    val radians = (hour / 24f) * (4f * Math.PI.toFloat()) - 1.2f
                    val tideVal = 1.25f + 0.95f * kotlin.math.sin(radians)
                    val x = progress * w
                    val y = tideToY(tideVal)
                    points.add(Offset(x, y))
                }

                curvePath.moveTo(points.first().x, points.first().y)
                fillPath.moveTo(points.first().x, points.first().y)

                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val cx = (prev.x + curr.x) / 2f
                    curvePath.quadraticBezierTo(prev.x, prev.y, cx, (prev.y + curr.y) / 2f)
                    fillPath.quadraticBezierTo(prev.x, prev.y, cx, (prev.y + curr.y) / 2f)
                }
                curvePath.lineTo(points.last().x, points.last().y)
                fillPath.lineTo(points.last().x, points.last().y)

                // Fecha o caminho para o preenchimento com gradiente de água
                fillPath.lineTo(w, effectiveH)
                fillPath.lineTo(0f, effectiveH)
                fillPath.close()

                // Desenha preenchimento aquático suave
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AquaCyan.copy(alpha = 0.35f),
                            AquaCyan.copy(alpha = 0.05f)
                        ),
                        startY = 0f,
                        endY = effectiveH
                    )
                )

                // Desenha a linha da onda
                drawPath(
                    path = curvePath,
                    color = OceanBlue,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                // 3. Indicador do Horário Atual no Gráfico
                val calendar = Calendar.getInstance()
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 60f
                val currentProgress = (currentHour / 24f).coerceIn(0f, 1f)
                val currentX = currentProgress * w
                val currentY = tideToY(currentHeight.toFloat())

                // Linha vertical indicadora do momento atual
                drawLine(
                    color = OceanBlue.copy(alpha = 0.4f),
                    start = Offset(currentX, 0f),
                    end = Offset(currentX, effectiveH),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                )

                // Círculo pulsante externo
                drawCircle(
                    color = AquaCyan.copy(alpha = pulseAlpha),
                    radius = 12.dp.toPx(),
                    center = Offset(currentX, currentY)
                )

                // Círculo interno sólido
                drawCircle(
                    color = OceanBlue,
                    radius = 5.dp.toPx(),
                    center = Offset(currentX, currentY)
                )

                // Ponto central branco
                drawCircle(
                    color = Color.White,
                    radius = 2.dp.toPx(),
                    center = Offset(currentX, currentY)
                )
            }

            // Marcadores de Horários no Rodapé do Gráfico
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "00h", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "06h", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "12h", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "18h", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "24h", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
