package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.MediaType
import com.example.data.VaultItem
import com.example.ui.theme.AmberWarning
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
fun VaultScreen(
  isUnlocked: Boolean,
  isDecoyVault: Boolean,
  pinInput: String,
  pinError: String?,
  selectedAlbum: String,
  vaultItems: List<VaultItem>,
  onPinDigit: (String) -> Unit,
  onPinDelete: () -> Unit,
  onUnlockBiometric: () -> Unit,
  onLockVault: () -> Unit,
  onSelectAlbum: (String) -> Unit,
  onOpenDetail: (VaultItem) -> Unit,
  onRestoreMedia: (String) -> Unit,
  onPermanentlyDelete: (String) -> Unit,
  onBackToSettings: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  if (!isUnlocked) {
    VaultLockGate(
      pinInput = pinInput,
      pinError = pinError,
      onPinDigit = onPinDigit,
      onPinDelete = onPinDelete,
      onUnlockBiometric = onUnlockBiometric,
      onBackToSettings = onBackToSettings,
      modifier = modifier
    )
  } else {
    VaultGalleryContent(
      isDecoyVault = isDecoyVault,
      selectedAlbum = selectedAlbum,
      vaultItems = vaultItems,
      onSelectAlbum = onSelectAlbum,
      onLockVault = onLockVault,
      onOpenDetail = onOpenDetail,
      onRestoreMedia = onRestoreMedia,
      onPermanentlyDelete = onPermanentlyDelete,
      onBackToSettings = onBackToSettings,
      modifier = modifier
    )
  }
}

@Composable
private fun VaultLockGate(
  pinInput: String,
  pinError: String?,
  onPinDigit: (String) -> Unit,
  onPinDelete: () -> Unit,
  onUnlockBiometric: () -> Unit,
  onBackToSettings: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val pulseTransition = rememberInfiniteTransition(label = "bioPulse")
  val pulseScale by pulseTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Back to Settings row if navigated from Settings
    if (onBackToSettings != null) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
      ) {
        IconButton(
          onClick = onBackToSettings,
          modifier = Modifier.testTag("vault_back_to_settings_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back to Settings",
            tint = TextPrimary
          )
        }
      }
    }

    // Header & Icon
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(top = if (onBackToSettings != null) 0.dp else 16.dp)
    ) {
      Box(
        modifier = Modifier
          .size(76.dp)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              listOf(EmeraldPrimary.copy(alpha = 0.25f), Color.Transparent)
            )
          )
          .border(1.5.dp, EmeraldPrimary, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Security,
          contentDescription = "Vault Security",
          tint = EmeraldPrimary,
          modifier = Modifier.size(38.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Private Encrypted Vault",
        fontSize = 20.sp,
        fontWeight = FontWeight.ExtraBold,
        color = TextPrimary
      )

      Text(
        text = "Hardware Keystore AES-256 GCM Protected",
        fontSize = 12.sp,
        color = CyanAccent,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(top = 4.dp)
      )

      // PIN Dots
      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 24.dp)
      ) {
        repeat(4) { index ->
          val isFilled = index < pinInput.length
          Box(
            modifier = Modifier
              .size(16.dp)
              .clip(CircleShape)
              .background(if (isFilled) EmeraldPrimary else ObsidianSurfaceVariant)
              .border(
                1.5.dp,
                if (isFilled) EmeraldPrimary else ObsidianBorder,
                CircleShape
              )
          )
        }
      }

      if (pinError != null) {
        Text(
          text = pinError,
          color = RoseDanger,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center
        )
      } else {
        Text(
          text = "Enter 4-Digit Security PIN (Default: 1234)",
          color = TextMuted,
          fontSize = 11.sp
        )
      }
    }

    // Keypad (1 - 9, Biometric, 0, Backspace)
    Column(
      verticalArrangement = Arrangement.spacedBy(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxWidth()
    ) {
      val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9")
      )

      rows.forEach { row ->
        Row(
          horizontalArrangement = Arrangement.spacedBy(24.dp),
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          row.forEach { digit ->
            KeypadButton(
              text = digit,
              onClick = { onPinDigit(digit) },
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      // Bottom Row: Biometrics, 0, Delete
      Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Biometrics Button
        Box(
          modifier = Modifier
            .weight(1f)
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(EmeraldDark.copy(alpha = 0.2f))
            .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable(onClick = onUnlockBiometric)
            .testTag("biometric_quick_unlock"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Fingerprint,
            contentDescription = "Biometrics Unlock",
            tint = EmeraldPrimary,
            modifier = Modifier
              .size(28.dp)
              .scale(pulseScale)
          )
        }

        KeypadButton(
          text = "0",
          onClick = { onPinDigit("0") },
          modifier = Modifier.weight(1f)
        )

        // Delete Button
        Box(
          modifier = Modifier
            .weight(1f)
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurfaceVariant)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onPinDelete)
            .testTag("pin_delete_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Backspace,
            contentDescription = "Backspace",
            tint = TextSecondary,
            modifier = Modifier.size(22.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun KeypadButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .height(58.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(ObsidianSurface)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("keypad_$text"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      fontSize = 22.sp,
      fontWeight = FontWeight.Bold,
      color = TextPrimary
    )
  }
}

@Composable
private fun VaultGalleryContent(
  isDecoyVault: Boolean,
  selectedAlbum: String,
  vaultItems: List<VaultItem>,
  onSelectAlbum: (String) -> Unit,
  onLockVault: () -> Unit,
  onOpenDetail: (VaultItem) -> Unit,
  onRestoreMedia: (String) -> Unit,
  onPermanentlyDelete: (String) -> Unit,
  onBackToSettings: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val albums = listOf("All Media", "Camera", "Wallpapers", "Secret Vault", "Downloads", "Trash")

  val filteredItems = vaultItems.filter { item ->
    if (selectedAlbum == "Trash") {
      item.isDeleted
    } else {
      !item.isDeleted && (selectedAlbum == "All Media" || item.album.equals(selectedAlbum, ignoreCase = true))
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Decoy Alert Banner
    if (isDecoyVault) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(AmberWarning.copy(alpha = 0.15f))
          .border(1.dp, AmberWarning)
          .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = "Decoy Mode",
          tint = AmberWarning,
          modifier = Modifier.size(18.dp)
        )
        Text(
          text = "Decoy Vault Mode Active. Sensitive albums hidden.",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = AmberWarning
        )
      }
    }

    // Top Controls: Back button, Lock button & Stats
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (onBackToSettings != null) {
          IconButton(
            onClick = onBackToSettings,
            modifier = Modifier
              .size(36.dp)
              .testTag("vault_gallery_back_to_settings")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Settings",
              tint = TextPrimary
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
        }

        Column {
          Text(
            text = if (selectedAlbum == "Trash") "30-Day Trash Bin" else "Hardware Safe",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
          )
          Text(
            text = "${filteredItems.size} items protected",
            fontSize = 12.sp,
            color = TextSecondary
          )
        }
      }

      OutlinedButton(
        onClick = onLockVault,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldPrimary),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.testTag("lock_vault_button")
      ) {
        Icon(
          imageVector = Icons.Default.LockOpen,
          contentDescription = "Lock",
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("Lock Vault", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    // Album Filter Pills
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
      items(albums) { album ->
        val isSelected = album == selectedAlbum
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) EmeraldPrimary else ObsidianSurfaceVariant)
            .border(
              1.dp,
              if (isSelected) EmeraldPrimary else ObsidianBorder,
              RoundedCornerShape(18.dp)
            )
            .clickable { onSelectAlbum(album) }
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("album_tab_$album")
        ) {
          Text(
            text = album,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.Black else TextSecondary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Gallery Grid
    if (filteredItems.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Empty",
            tint = TextMuted,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = if (selectedAlbum == "Trash") "Trash is empty." else "No encrypted media in this album.",
            color = TextMuted,
            fontSize = 14.sp
          )
        }
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp)
      ) {
        items(filteredItems, key = { it.id }) { item ->
          VaultGridCard(
            item = item,
            isTrash = selectedAlbum == "Trash",
            onClick = { onOpenDetail(item) },
            onRestore = { onRestoreMedia(item.id) },
            onPermanentDelete = { onPermanentlyDelete(item.id) }
          )
        }
      }
    }
  }
}

@Composable
private fun VaultGridCard(
  item: VaultItem,
  isTrash: Boolean,
  onClick: () -> Unit,
  onRestore: () -> Unit,
  onPermanentDelete: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f)
      .clip(RoundedCornerShape(14.dp))
      .background(ObsidianSurface)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag("vault_item_${item.id}")
  ) {
    AsyncImage(
      model = item.mediaUrl,
      contentDescription = item.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // Dark Gradient Overlay
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.85f))
          )
        )
    )

    // Top Badges
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Encryption Tag
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(Color.Black.copy(alpha = 0.7f))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Encrypted",
            tint = EmeraldPrimary,
            modifier = Modifier.size(10.dp)
          )
          Text("AES-256", fontSize = 9.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
        }
      }

      if (item.isFavorite) {
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = "Favorite",
          tint = AmberWarning,
          modifier = Modifier.size(16.dp)
        )
      }
    }

    // Video duration or Compression tag
    if (item.type == MediaType.VIDEO) {
      Box(
        modifier = Modifier
          .align(Alignment.Center)
          .size(34.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.PlayArrow,
          contentDescription = "Play",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // Bottom info
    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Text(
        text = item.title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        maxLines = 1
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = item.formattedCompressedSize,
          fontSize = 10.sp,
          color = CyanAccent,
          fontWeight = FontWeight.SemiBold
        )

        Text(
          text = "-${item.compressionPercentage}%",
          fontSize = 9.sp,
          color = EmeraldPrimary,
          fontWeight = FontWeight.Bold
        )
      }

      // Trash Actions
      if (isTrash) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          IconButton(
            onClick = onRestore,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Restore,
              contentDescription = "Restore",
              tint = EmeraldPrimary,
              modifier = Modifier.size(16.dp)
            )
          }

          IconButton(
            onClick = onPermanentDelete,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.DeleteForever,
              contentDescription = "Shred",
              tint = RoseDanger,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}
