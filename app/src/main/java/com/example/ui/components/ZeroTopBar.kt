package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun ZeroTopBar(
  isAmoledMode: Boolean,
  unreadMessagesCount: Int,
  userEmail: String? = null,
  onToggleAmoled: () -> Unit,
  onOpenMessenger: () -> Unit,
  onOpenSettings: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseAlpha"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .border(1.dp, ObsidianBorder.copy(alpha = 0.5f))
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Logo & Sync Status
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(EmeraldPrimary.copy(alpha = 0.15f))
            .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Zero Shield",
            tint = EmeraldPrimary,
            modifier = Modifier.size(18.dp)
          )
        }

        Text(
          text = "ZERO",
          fontWeight = FontWeight.ExtraBold,
          fontSize = 20.sp,
          letterSpacing = 1.5.sp,
          color = TextPrimary
        )

        // Cloud Synced Pill
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF16202E))
            .border(1.dp, CyanAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary)
              .alpha(pulseAlpha)
          )
          Text(
            text = if (!userEmail.isNullOrBlank()) userEmail.substringBefore("@") else "E2EE Cloud",
            color = CyanAccent,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
          )
        }
      }

      // Right: Actions
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // AMOLED Toggle
        IconButton(
          onClick = onToggleAmoled,
          modifier = Modifier.testTag("amoled_toggle_button")
        ) {
          Icon(
            imageVector = if (isAmoledMode) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = "Toggle AMOLED Mode",
            tint = if (isAmoledMode) EmeraldPrimary else TextMuted,
            modifier = Modifier.size(20.dp)
          )
        }

        // Messenger Icon with Badge
        BadgedBox(
          badge = {
            if (unreadMessagesCount > 0) {
              Badge(
                containerColor = EmeraldPrimary,
                contentColor = Color.Black
              ) {
                Text(
                  text = unreadMessagesCount.toString(),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        ) {
          IconButton(
            onClick = onOpenMessenger,
            modifier = Modifier.testTag("messenger_button")
          ) {
            Icon(
              imageVector = Icons.Default.ChatBubbleOutline,
              contentDescription = "Direct Messenger",
              tint = TextPrimary,
              modifier = Modifier.size(22.dp)
            )
          }
        }

        // Settings Icon
        IconButton(
          onClick = onOpenSettings,
          modifier = Modifier.testTag("settings_top_button")
        ) {
          Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = TextPrimary,
            modifier = Modifier.size(22.dp)
          )
        }
      }
    }
  }
}
