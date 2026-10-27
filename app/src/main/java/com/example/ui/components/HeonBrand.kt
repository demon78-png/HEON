package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonTextPrimary

/**
 * Original HEON geometric gaming emblem
 */
@Composable
fun HeonEmblem(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF141824), Color(0xFF0E1018))
                ),
                shape = RoundedCornerShape(size * 0.28f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.72f)) {
            val w = this.size.width
            val h = this.size.height

            // Left vertical blade of H
            val leftBlade = Path().apply {
                moveTo(w * 0.15f, h * 0.1f)
                lineTo(w * 0.38f, h * 0.1f)
                lineTo(w * 0.38f, h * 0.9f)
                lineTo(w * 0.15f, h * 0.75f)
                close()
            }
            drawPath(
                path = leftBlade,
                brush = Brush.verticalGradient(listOf(HeonAccentNeon, Color(0xFF67B008)))
            )

            // Right vertical blade of H
            val rightBlade = Path().apply {
                moveTo(w * 0.62f, h * 0.1f)
                lineTo(w * 0.85f, h * 0.25f)
                lineTo(w * 0.85f, h * 0.9f)
                lineTo(w * 0.62f, h * 0.9f)
                close()
            }
            drawPath(
                path = rightBlade,
                brush = Brush.verticalGradient(listOf(HeonAccentPurple, Color(0xFF581C87)))
            )

            // Center neon crossbar with targeting diamond
            drawLine(
                color = HeonAccentNeon,
                start = Offset(w * 0.32f, h * 0.5f),
                end = Offset(w * 0.68f, h * 0.5f),
                strokeWidth = w * 0.12f
            )

            // Center core node
            drawCircle(
                color = Color.White,
                radius = w * 0.08f,
                center = Offset(w * 0.5f, h * 0.5f)
            )
        }
    }
}

/**
 * HEON Brand Wordmark
 */
@Composable
fun HeonWordmark(
    modifier: Modifier = Modifier,
    showEmblem: Boolean = true,
    fontSize: Float = 22f
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showEmblem) {
            HeonEmblem(size = (fontSize * 1.35f).dp)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "H",
                color = HeonAccentNeon,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )
            Text(
                text = "EON",
                color = HeonTextPrimary,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )
        }
    }
}
