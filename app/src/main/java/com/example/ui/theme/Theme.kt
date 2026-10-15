package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ObsidianColorScheme = darkColorScheme(
  primary = EmeraldPrimary,
  onPrimary = Color.Black,
  primaryContainer = EmeraldDark,
  onPrimaryContainer = Color.White,
  secondary = CyanAccent,
  onSecondary = Color.Black,
  secondaryContainer = ObsidianSurfaceVariant,
  onSecondaryContainer = CyanLight,
  tertiary = PurpleAccent,
  background = ObsidianBg,
  onBackground = TextPrimary,
  surface = ObsidianSurface,
  onSurface = TextPrimary,
  surfaceVariant = ObsidianSurfaceVariant,
  onSurfaceVariant = TextSecondary,
  outline = ObsidianBorder,
  error = RoseDanger,
  onError = Color.White
)

private val AmoledColorScheme = darkColorScheme(
  primary = EmeraldPrimary,
  onPrimary = Color.Black,
  primaryContainer = EmeraldDark,
  onPrimaryContainer = Color.White,
  secondary = CyanAccent,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF111111),
  onSecondaryContainer = CyanLight,
  tertiary = PurpleAccent,
  background = PureBlackBg,
  onBackground = TextPrimary,
  surface = PureBlackBg,
  onSurface = TextPrimary,
  surfaceVariant = Color(0xFF121212),
  onSurfaceVariant = TextSecondary,
  outline = Color(0xFF262626),
  error = RoseDanger,
  onError = Color.White
)

@Composable
fun ZEROTheme(
  isAmoledMode: Boolean = false,
  content: @Composable () -> Unit
) {
  val colors = if (isAmoledMode) AmoledColorScheme else ObsidianColorScheme

  MaterialTheme(
    colorScheme = colors,
    typography = Typography,
    content = content
  )
}
