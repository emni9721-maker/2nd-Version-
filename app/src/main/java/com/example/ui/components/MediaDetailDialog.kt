package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.MediaType
import com.example.data.VaultItem
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MediaDetailDialog(
  item: VaultItem,
  onDismiss: () -> Unit,
  onToggleFavorite: () -> Unit,
  onSoftDelete: () -> Unit,
  onShareToFeed: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.95f))
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(bottom = 24.dp)
      ) {
        // Top Action Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.1f))
              .testTag("close_detail_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color.White
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Favorite Toggle
            IconButton(
              onClick = onToggleFavorite,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f))
                .testTag("toggle_favorite_detail")
            ) {
              Icon(
                imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite",
                tint = if (item.isFavorite) AmberWarning else Color.White
              )
            }

            // Move to Trash
            IconButton(
              onClick = onSoftDelete,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(RoseDanger.copy(alpha = 0.2f))
                .testTag("delete_detail_button")
            ) {
              Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Trash",
                tint = RoseDanger
              )
            }
          }
        }

        // Large Media Viewer
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .background(ObsidianSurfaceVariant)
        ) {
          AsyncImage(
            model = item.mediaUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
          )

          if (item.type == MediaType.VIDEO) {
            Box(
              modifier = Modifier
                .align(Alignment.Center)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
                .border(2.dp, EmeraldPrimary, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Video",
                tint = EmeraldPrimary,
                modifier = Modifier.size(32.dp)
              )
            }
          }
        }

        // Title & 1-Tap Share to Social Feed
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = item.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "${item.album} Album • ${item.dateFormatted}",
                fontSize = 12.sp,
                color = TextSecondary
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(EmeraldPrimary.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "-${item.compressionPercentage}% Saved",
                fontSize = 11.sp,
                color = EmeraldPrimary,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // 1-Tap Share to Social Feed Button
          Button(
            onClick = onShareToFeed,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("share_to_feed_button")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              tint = Color.Black,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Publish to Public Social Feed", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          }

          // EXIF & Cryptographic Metadata Sheet
          Card(
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = "EXIF info",
                  tint = CyanAccent,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = "EXIF & Security Metadata",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              }

              MetaRow(label = "Resolution", value = item.exifData.resolution)
              MetaRow(label = "Exposure & ISO", value = "${item.exifData.aperture} • ${item.exifData.shutterSpeed} • ${item.exifData.iso}")
              MetaRow(label = "Camera Sensor", value = item.exifData.cameraModel)
              MetaRow(label = "Original Size", value = item.formattedOriginalSize)
              MetaRow(label = "Encrypted Size", value = item.formattedCompressedSize)
              MetaRow(label = "Cipher Algorithm", value = item.encryptionAlgorithm)
              MetaRow(label = "Master Key Alias", value = item.keyAlias)
              MetaRow(label = "SHA-256 Checksum", value = item.sha256Hash)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun MetaRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = label, fontSize = 11.sp, color = TextMuted)
    Text(text = value, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
  }
}
