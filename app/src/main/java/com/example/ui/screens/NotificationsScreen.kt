package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppNotification
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun NotificationsScreen(
  notifications: List<AppNotification>,
  onBackToFeed: (() -> Unit)? = null,
  onClearAll: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  BackHandler(enabled = onBackToFeed != null) {
    onBackToFeed?.invoke()
  }

  var selectedFilter by remember { mutableStateOf("All") }
  val filters = listOf("All", "Unread", "Files", "Backup", "Security")

  val filteredList = remember(notifications, selectedFilter) {
    when (selectedFilter) {
      "Unread" -> notifications.filter { !it.isRead }
      "Files" -> notifications.filter { it.iconType == "FILE" }
      "Backup" -> notifications.filter { it.iconType == "BACKUP" }
      "Security" -> notifications.filter { it.iconType == "VAULT" || it.iconType == "CLOUD" }
      else -> notifications
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("notifications_screen")
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (onBackToFeed != null) {
          IconButton(
            onClick = onBackToFeed,
            modifier = Modifier.testTag("notifications_back_to_feed")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Feed",
              tint = TextPrimary
            )
          }
        }

        Column {
          Text(
            text = "Notifications",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Cloud backups, storage & original files",
            fontSize = 11.sp,
            color = TextSecondary
          )
        }
      }

      IconButton(onClick = onClearAll) {
        Icon(
          imageVector = Icons.Default.DoneAll,
          contentDescription = "Mark all as read",
          tint = CyanAccent,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // Filter Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    ) {
      items(filters) { filter ->
        val isSelected = filter == selectedFilter
        FilterChip(
          selected = isSelected,
          onClick = { selectedFilter = filter },
          label = { Text(filter, fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary.copy(alpha = 0.2f),
            selectedLabelColor = EmeraldPrimary,
            containerColor = ObsidianSurfaceVariant,
            labelColor = TextMuted
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) EmeraldPrimary else ObsidianBorder
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Notifications List
    if (filteredList.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text("No notifications yet", fontSize = 14.sp, color = TextMuted)
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredList, key = { it.id }) { notif ->
          Card(
            colors = CardDefaults.cardColors(
              containerColor = if (!notif.isRead) ObsidianSurfaceElevated else ObsidianSurface
            ),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (!notif.isRead) EmeraldPrimary.copy(alpha = 0.3f) else ObsidianBorder
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(
                    when (notif.iconType) {
                      "FILE" -> Color(0xFF7C3AED).copy(alpha = 0.2f)
                      "BACKUP" -> EmeraldPrimary.copy(alpha = 0.2f)
                      "VAULT" -> CyanAccent.copy(alpha = 0.2f)
                      "SOCIAL" -> RoseDanger.copy(alpha = 0.2f)
                      else -> Color(0xFF1E293B)
                    }
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (notif.iconType) {
                    "FILE" -> Icons.AutoMirrored.Filled.InsertDriveFile
                    "BACKUP" -> Icons.Default.CloudSync
                    "VAULT" -> Icons.Default.Lock
                    "SOCIAL" -> Icons.Default.Favorite
                    else -> Icons.Default.Notifications
                  },
                  contentDescription = notif.title,
                  tint = when (notif.iconType) {
                    "FILE" -> Color(0xFF7C3AED)
                    "BACKUP" -> EmeraldPrimary
                    "VAULT" -> CyanAccent
                    "SOCIAL" -> RoseDanger
                    else -> TextSecondary
                  },
                  modifier = Modifier.size(20.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = notif.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                  Text(
                    text = notif.time,
                    fontSize = 10.sp,
                    color = TextMuted
                  )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = notif.description,
                  fontSize = 12.sp,
                  color = TextSecondary
                )
              }

              if (!notif.isRead) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary)
                )
              }
            }
          }
        }
      }
    }
  }
}
