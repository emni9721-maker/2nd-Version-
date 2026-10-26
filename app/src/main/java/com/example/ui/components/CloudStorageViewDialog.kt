package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class CloudBackupItem(
  val id: String,
  val fileName: String,
  val mediaUrl: String,
  val folder: String,
  val isVideo: Boolean,
  val formattedSize: String,
  val backedUpDate: String
)

@Composable
fun CloudStorageViewDialog(
  onDismiss: () -> Unit
) {
  var selectedCategory by remember { mutableStateOf("All") }
  val categories = listOf("All", "Camera", "Screenshots", "Videos")

  val sampleBackupItems = remember {
    listOf(
      CloudBackupItem("cb_1", "IMG_2026_1022_091.jpg", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80", "Camera", false, "4.2 MB", "Today, 10:45 AM"),
      CloudBackupItem("cb_2", "VID_2026_1021_044.mp4", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=800&q=80", "Videos", true, "38.5 MB", "Today, 09:12 AM"),
      CloudBackupItem("cb_3", "SCR_2026_1020_118.png", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80", "Screenshots", false, "1.8 MB", "Yesterday"),
      CloudBackupItem("cb_4", "IMG_2026_1019_002.jpg", "https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=800&q=80", "Camera", false, "5.6 MB", "Oct 19"),
      CloudBackupItem("cb_5", "VID_2026_1018_912.mp4", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?auto=format&fit=crop&w=800&q=80", "Videos", true, "64.1 MB", "Oct 18"),
      CloudBackupItem("cb_6", "IMG_2026_1017_441.jpg", "https://images.unsplash.com/photo-1511447333015-45b65e60f6d5?auto=format&fit=crop&w=800&q=80", "Camera", false, "3.1 MB", "Oct 17"),
      CloudBackupItem("cb_7", "SCR_2026_1016_883.png", "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=800&q=80", "Screenshots", false, "2.4 MB", "Oct 16"),
      CloudBackupItem("cb_8", "IMG_2026_1015_239.jpg", "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=800&q=80", "Camera", false, "6.8 MB", "Oct 15")
    )
  }

  val filteredItems = remember(selectedCategory) {
    if (selectedCategory == "All") sampleBackupItems
    else if (selectedCategory == "Videos") sampleBackupItems.filter { it.isVideo }
    else sampleBackupItems.filter { it.folder.equals(selectedCategory, ignoreCase = true) }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .testTag("cloud_storage_view_dialog"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(CyanAccent.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CloudDone,
                contentDescription = null,
                tint = CyanAccent,
                modifier = Modifier.size(22.dp)
              )
            }
            Column {
              Text(
                text = "Cloud Storage View",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
              )
              Text(
                text = "Automatic Cloud Backup Media Library",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_cloud_storage_view")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Status Banner
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = EmeraldPrimary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Backup Status: Up to date",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = EmeraldPrimary
                )
              }
              Text(
                text = "18.4 GB used • 142 photos & videos backed up",
                fontSize = 11.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(CyanAccent.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "Google Photos Sync",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          categories.forEach { cat ->
            val isSelected = selectedCategory == cat
            FilterChip(
              selected = isSelected,
              onClick = { selectedCategory = cat },
              label = {
                Text(
                  text = cat,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.Black else TextPrimary
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = CyanAccent,
                containerColor = MaterialTheme.colorScheme.background
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = ObsidianBorder,
                selectedBorderColor = CyanAccent,
                enabled = true,
                selected = isSelected
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of Backed Up Media Items
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 8.dp)
        ) {
          items(filteredItems, key = { it.id }) { item ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp)),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
              Column {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                ) {
                  AsyncImage(
                    model = item.mediaUrl,
                    contentDescription = item.fileName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                  )

                  // Video Badge or Cloud Done Icon
                  Box(
                    modifier = Modifier
                      .align(Alignment.TopEnd)
                      .padding(6.dp)
                      .clip(CircleShape)
                      .background(Color.Black.copy(alpha = 0.6f))
                      .padding(4.dp)
                  ) {
                    Icon(
                      imageVector = if (item.isVideo) Icons.Default.PlayCircle else Icons.Default.CheckCircle,
                      contentDescription = null,
                      tint = if (item.isVideo) CyanAccent else EmeraldPrimary,
                      modifier = Modifier.size(14.dp)
                    )
                  }

                  // Folder Tag
                  Box(
                    modifier = Modifier
                      .align(Alignment.BottomStart)
                      .padding(6.dp)
                      .clip(RoundedCornerShape(4.dp))
                      .background(Color.Black.copy(alpha = 0.7f))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = item.folder,
                      fontSize = 9.sp,
                      color = TextPrimary,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }

                Column(modifier = Modifier.padding(8.dp)) {
                  Text(
                    text = item.fileName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1
                  )
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = item.formattedSize,
                      fontSize = 10.sp,
                      color = TextSecondary
                    )
                    Text(
                      text = item.backedUpDate,
                      fontSize = 9.sp,
                      color = TextMuted
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
