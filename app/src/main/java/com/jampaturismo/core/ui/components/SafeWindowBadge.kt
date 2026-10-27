package com.jampaturismo.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jampaturismo.core.theme.tideColors
import com.jampaturismo.data.model.TideSafetyLevel

@Composable
fun SafeWindowBadge(
    safetyLevel: TideSafetyLevel,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label, icon) = when (safetyLevel) {
        TideSafetyLevel.SAFE -> Quadruple(
            MaterialTheme.tideColors.safe.copy(alpha = 0.15f),
            MaterialTheme.tideColors.safe,
            "Janela Segura (Travessia Liberada)",
            Icons.Default.CheckCircle
        )
        TideSafetyLevel.WARNING -> Quadruple(
            MaterialTheme.tideColors.warning.copy(alpha = 0.15f),
            MaterialTheme.tideColors.warning,
            "Atenção (Maré Virando)",
            Icons.Default.WarningAmber
        )
        TideSafetyLevel.DANGER -> Quadruple(
            MaterialTheme.tideColors.danger.copy(alpha = 0.15f),
            MaterialTheme.tideColors.danger,
            "Perigo / Retorne (Água Subindo)",
            Icons.Default.Warning
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
