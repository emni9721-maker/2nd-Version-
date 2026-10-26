package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary

/**
 * Uncommon, high-tech geometric "Z" monogram logo.
 * Custom vector geometry rendered with Compose Canvas featuring cybernetic facets,
 * neon gradient sweeps, and ambient pulse. Replaces standard lock iconography.
 */
@Composable
fun ZeroZLogo(
  modifier: Modifier = Modifier,
  size: Dp = 76.dp,
  enablePulse: Boolean = true
) {
  val pulseTransition = rememberInfiniteTransition(label = "zPulse")
  val pulseScale by pulseTransition.animateFloat(
    initialValue = if (enablePulse) 0.96f else 1.0f,
    targetValue = if (enablePulse) 1.04f else 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Box(
    modifier = modifier
      .size(size)
      .clip(CircleShape)
      .background(
        Brush.radialGradient(
          colors = listOf(
            EmeraldPrimary.copy(alpha = 0.28f),
            CyanAccent.copy(alpha = 0.12f),
            Color.Transparent
          )
        )
      )
      .border(
        width = 2.dp,
        brush = Brush.linearGradient(
          colors = listOf(EmeraldPrimary, CyanAccent, EmeraldPrimary)
        ),
        shape = CircleShape
      ),
    contentAlignment = Alignment.Center
  ) {
    Canvas(
      modifier = Modifier
        .size(size * 0.58f)
        .scale(pulseScale)
    ) {
      drawCyberZLogo()
    }
  }
}

private fun DrawScope.drawCyberZLogo() {
  val w = size.width
  val h = size.height

  // Gradient colors for the geometric Z
  val zGradient = Brush.linearGradient(
    colors = listOf(EmeraldPrimary, CyanAccent),
    start = Offset(0f, 0f),
    end = Offset(w, h)
  )

  val accentGlow = CyanAccent.copy(alpha = 0.75f)

  // 1. Faceted Cyber "Z" Path (Thick, stylized geometric lettermark)
  val zPath = Path().apply {
    // Top Bar (Left to Right with bevel cut)
    moveTo(w * 0.14f, h * 0.18f)
    lineTo(w * 0.86f, h * 0.18f)
    lineTo(w * 0.86f, h * 0.32f)
    // Diagonal to bottom-left with sharp velocity angle
    lineTo(w * 0.40f, h * 0.68f)
    lineTo(w * 0.86f, h * 0.68f)
    // Bottom Bar (Right to Left)
    lineTo(w * 0.86f, h * 0.82f)
    lineTo(w * 0.14f, h * 0.82f)
    lineTo(w * 0.14f, h * 0.68f)
    // Diagonal return
    lineTo(w * 0.60f, h * 0.32f)
    lineTo(w * 0.14f, h * 0.32f)
    close()
  }

  // Draw Main Z Body
  drawPath(
    path = zPath,
    brush = zGradient,
    style = Fill
  )

  // Subtle inner stroke for crisp cyber edges
  drawPath(
    path = zPath,
    color = Color.White.copy(alpha = 0.35f),
    style = Stroke(
      width = 1.5.dp.toPx(),
      cap = StrokeCap.Round,
      join = StrokeJoin.Round
    )
  )

  // 2. High-Tech Cybernetic Accent Slits & Nodes
  // Top-right micro node
  drawCircle(
    color = accentGlow,
    radius = w * 0.05f,
    center = Offset(w * 0.86f, h * 0.25f)
  )

  // Bottom-left micro node
  drawCircle(
    color = EmeraldPrimary,
    radius = w * 0.05f,
    center = Offset(w * 0.14f, h * 0.75f)
  )

  // Center diagonal energy slice line
  drawLine(
    color = Color.White.copy(alpha = 0.8f),
    start = Offset(w * 0.48f, h * 0.45f),
    end = Offset(w * 0.52f, h * 0.55f),
    strokeWidth = 2.dp.toPx(),
    cap = StrokeCap.Round
  )
}
