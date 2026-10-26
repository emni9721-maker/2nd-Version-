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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class UploadCategory(
  val label: String,
  val subtitle: String,
  val icon: ImageVector,
  val mediaType: MediaType
) {
  POST("Post", "Community feed photo", Icons.Default.PhotoLibrary, MediaType.IMAGE),
  STORIES("Stories", "24-hour disappearing story", Icons.Default.AutoAwesome, MediaType.IMAGE),
  REELS("Reels", "Vertical video clip", Icons.Default.VideoLibrary, MediaType.VIDEO),
  VIDEO("Video", "Encrypted cloud video", Icons.Default.Videocam, MediaType.VIDEO)
}

@Composable
fun UploadModal(
  draft: UploadDraft,
  onUpdateDraft: (UploadDraft) -> Unit,
  onSubmit: () -> Unit,
  onDismiss: () -> Unit
) {
  var selectedCategory by remember { mutableStateOf(UploadCategory.POST) }

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(EmeraldDark.copy(alpha = 0.35f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Text(
                text = "Create & Upload",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "Select what you want to upload",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(28.dp)
              .testTag("upload_modal_close")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextMuted
            )
          }
        }

        // The 4 Upload Categories (Post, Stories, Reels, Video)
        Text(
          text = "Upload Category",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          // Row 1: Post & Stories
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            UploadCategoryCard(
              category = UploadCategory.POST,
              isSelected = selectedCategory == UploadCategory.POST,
              onClick = {
                selectedCategory = UploadCategory.POST
                onUpdateDraft(draft.copy(type = MediaType.IMAGE, album = "Posts"))
              },
              modifier = Modifier.weight(1f)
            )

            UploadCategoryCard(
              category = UploadCategory.STORIES,
              isSelected = selectedCategory == UploadCategory.STORIES,
              onClick = {
                selectedCategory = UploadCategory.STORIES
                onUpdateDraft(draft.copy(type = MediaType.IMAGE, album = "Stories"))
              },
              modifier = Modifier.weight(1f)
            )
          }

          // Row 2: Reels & Video
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            UploadCategoryCard(
              category = UploadCategory.REELS,
              isSelected = selectedCategory == UploadCategory.REELS,
              onClick = {
                selectedCategory = UploadCategory.REELS
                onUpdateDraft(draft.copy(type = MediaType.VIDEO, album = "Reels"))
              },
              modifier = Modifier.weight(1f)
            )

            UploadCategoryCard(
              category = UploadCategory.VIDEO,
              isSelected = selectedCategory == UploadCategory.VIDEO,
              onClick = {
                selectedCategory = UploadCategory.VIDEO
                onUpdateDraft(draft.copy(type = MediaType.VIDEO, album = "Videos"))
              },
              modifier = Modifier.weight(1f)
            )
          }
        }

        // Caption / Title Input
        OutlinedTextField(
          value = draft.title,
          onValueChange = { onUpdateDraft(draft.copy(title = it)) },
          label = { Text("${selectedCategory.label} Caption or Title", fontSize = 12.sp, color = TextMuted) },
          placeholder = { Text("What's on your mind? Add tags #...", fontSize = 12.sp, color = TextMuted) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("upload_title_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EmeraldPrimary,
            unfocusedBorderColor = ObsidianBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          ),
          shape = RoundedCornerShape(12.dp)
        )

        // Submit Button
        Button(
          onClick = onSubmit,
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("submit_upload_button")
        ) {
          Icon(
            imageVector = Icons.Default.CloudUpload,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Publish ${selectedCategory.label}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
          )
        }
      }
    }
  }
}

@Composable
private fun UploadCategoryCard(
  category: UploadCategory,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(if (isSelected) EmeraldDark.copy(alpha = 0.35f) else MaterialTheme.colorScheme.background)
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) EmeraldPrimary else ObsidianBorder,
        shape = RoundedCornerShape(12.dp)
      )
      .clickable(onClick = onClick)
      .padding(12.dp)
      .testTag("upload_category_${category.name.lowercase()}"),
    contentAlignment = Alignment.CenterStart
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(if (isSelected) EmeraldPrimary else ObsidianSurface),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = category.icon,
          contentDescription = category.label,
          tint = if (isSelected) Color.Black else TextSecondary,
          modifier = Modifier.size(18.dp)
        )
      }

      Column {
        Text(
          text = category.label,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = if (isSelected) EmeraldPrimary else TextPrimary
        )
        Text(
          text = category.subtitle,
          fontSize = 10.sp,
          color = TextSecondary,
          maxLines = 1
        )
      }
    }
  }
}
