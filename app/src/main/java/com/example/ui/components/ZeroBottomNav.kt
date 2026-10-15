package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun ZeroBottomNav(
  currentTab: AppTab,
  onSelectTab: (AppTab) -> Unit,
  onOpenUpload: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .border(1.dp, ObsidianBorder.copy(alpha = 0.6f))
      .navigationBarsPadding()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Tab 1: Feed
      NavTabItem(
        label = "Feed",
        icon = Icons.Default.DynamicFeed,
        isSelected = currentTab == AppTab.FEED,
        onClick = { onSelectTab(AppTab.FEED) },
        testTag = "nav_tab_feed"
      )

      // Tab 2: Vault
      NavTabItem(
        label = "Vault",
        icon = Icons.Default.Lock,
        isSelected = currentTab == AppTab.VAULT,
        onClick = { onSelectTab(AppTab.VAULT) },
        testTag = "nav_tab_vault"
      )

      // Center Action Button: Upload / Create
      Box(
        modifier = Modifier
          .size(48.dp)
          .shadow(8.dp, CircleShape)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              listOf(EmeraldPrimary, CyanAccent)
            )
          )
          .clickable(onClick = onOpenUpload)
          .testTag("nav_center_upload_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Upload Encrypted Media",
          tint = Color.Black,
          modifier = Modifier.size(28.dp)
        )
      }

      // Tab 3: Reels
      NavTabItem(
        label = "Reels",
        icon = Icons.Default.VideoLibrary,
        isSelected = currentTab == AppTab.REELS,
        onClick = { onSelectTab(AppTab.REELS) },
        testTag = "nav_tab_reels"
      )

      // Tab 4: Profile
      NavTabItem(
        label = "Profile",
        icon = Icons.Default.Person,
        isSelected = currentTab == AppTab.PROFILE,
        onClick = { onSelectTab(AppTab.PROFILE) },
        testTag = "nav_tab_profile"
      )
    }
  }
}

@Composable
private fun NavTabItem(
  label: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .testTag(testTag)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = if (isSelected) EmeraldPrimary else TextMuted,
      modifier = Modifier.size(24.dp)
    )
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) EmeraldPrimary else TextMuted,
      modifier = Modifier.padding(top = 2.dp)
    )
  }
}
