package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.MediaType
import com.example.ui.UploadDraft
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun UploadModal(
  draft: UploadDraft,
  onUpdateDraft: (UploadDraft) -> Unit,
  onSubmit: () -> Unit,
  onDismiss: () -> Unit
) {
  val albums = listOf("Camera", "Secret Vault", "Wallpapers", "Downloads")

  val originalMb = draft.originalSizeBytes.toDouble() / (1024 * 1024)
  val compressedMb = draft.compressedSizeBytes.toDouble() / (1024 * 1024)
  val percentSaved = (((draft.originalSizeBytes - draft.compressedSizeBytes).toDouble() / draft.originalSizeBytes) * 100).toInt()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
      shape = RoundedCornerShape(20.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("upload_modal_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "Upload",
              tint = EmeraldPrimary,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = "Encrypt & Store Media",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextMuted
            )
          }
        }

        // File Title
        OutlinedTextField(
          value = draft.title,
          onValueChange = { onUpdateDraft(draft.copy(title = it)) },
          label = { Text("File Name / Title", fontSize = 12.sp, color = TextMuted) },
          placeholder = { Text("e.g. Masterpiece_RAW_01.jpg", fontSize = 12.sp, color = TextMuted) },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EmeraldPrimary,
            unfocusedBorderColor = ObsidianBorder
          ),
          shape = RoundedCornerShape(12.dp)
        )

        // Type Selector (Photo vs Video)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (draft.type == MediaType.IMAGE) EmeraldDark.copy(alpha = 0.3f) else ObsidianSurfaceVariant)
              .border(1.dp, if (draft.type == MediaType.IMAGE) EmeraldPrimary else ObsidianBorder, RoundedCornerShape(10.dp))
              .clickable { onUpdateDraft(draft.copy(type = MediaType.IMAGE)) }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Photo",
                tint = if (draft.type == MediaType.IMAGE) EmeraldPrimary else TextSecondary,
                modifier = Modifier.size(16.dp)
              )
              Text("Photo (RAW/PNG)", fontSize = 11.sp, color = if (draft.type == MediaType.IMAGE) EmeraldPrimary else TextSecondary, fontWeight = FontWeight.Bold)
            }
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (draft.type == MediaType.VIDEO) CyanAccent.copy(alpha = 0.2f) else ObsidianSurfaceVariant)
              .border(1.dp, if (draft.type == MediaType.VIDEO) CyanAccent else ObsidianBorder, RoundedCornerShape(10.dp))
              .clickable { onUpdateDraft(draft.copy(type = MediaType.VIDEO)) }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = "Video",
                tint = if (draft.type == MediaType.VIDEO) CyanAccent else TextSecondary,
                modifier = Modifier.size(16.dp)
              )
              Text("4K Video (MP4)", fontSize = 11.sp, color = if (draft.type == MediaType.VIDEO) CyanAccent else TextSecondary, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Album Pills
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(text = "Target Vault Album", fontSize = 12.sp, color = TextMuted)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            albums.forEach { album ->
              val isSelected = draft.album == album
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) EmeraldPrimary else ObsidianSurfaceVariant)
                  .clickable { onUpdateDraft(draft.copy(album = album)) }
                  .padding(horizontal = 8.dp, vertical = 6.dp)
              ) {
                Text(
                  text = album,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.Black else TextSecondary
                )
              }
            }
          }
        }

        // Smart Compression Pipeline Preview Card
        Card(
          colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceVariant),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Compress,
                  contentDescription = "Compress",
                  tint = CyanAccent,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = "Compression Pipeline",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(EmeraldPrimary.copy(alpha = 0.2f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "-$percentSaved% SAVED",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = EmeraldPrimary
                )
              }
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Original: ${String.format("%.1f", originalMb)} MB",
                fontSize = 11.sp,
                color = TextMuted
              )
              Text(
                text = "Encrypted: ${String.format("%.1f", compressedMb)} MB",
                fontSize = 11.sp,
                color = CyanAccent,
                fontWeight = FontWeight.Bold
              )
            }

            LinearProgressIndicator(
              progress = { 0.15f },
              modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
              color = EmeraldPrimary,
              trackColor = ObsidianBorder
            )
          }
        }

        // Submit Button
        Button(
          onClick = onSubmit,
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("submit_upload_button")
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Store",
            tint = Color.Black,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Encrypt & Commit to Vault",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
          )
        }
      }
    }
  }
}
