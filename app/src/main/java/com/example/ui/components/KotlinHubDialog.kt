package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
fun KotlinHubDialog(
  onDismiss: () -> Unit
) {
  val files = listOf(
    ".github/workflows/main.yml",
    "CryptoVaultManager.kt",
    "BiometricHelper.kt",
    "VaultScreen.kt",
    "ReelsScreen.kt"
  )

  var selectedFile by remember { mutableStateOf(files.first()) }

  val codeSnippets = mapOf(
    ".github/workflows/main.yml" to """
# Resolved GitHub Actions Build Workflow
name: Android APK Build
on: [push, pull_request, workflow_dispatch]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
          cache: 'gradle'
      - uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '8.10.2'
      - run: chmod +x gradlew || true
      - name: Build Android APK
        run: |
          ./gradlew assembleRelease || ./gradlew assembleDebug
      - name: Upload APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: ZERO-Android-APK
          path: '**/build/outputs/apk/**/*.apk'
          if-no-files-found: error
    """.trimIndent(),

    "CryptoVaultManager.kt" to """
package com.example.data

object CryptoVaultManager {
  private const val ANDROID_KEYSTORE = "AndroidKeyStore"
  private const val MASTER_KEY_ALIAS = "ZERO_Hardware_Master_Key"
  private const val AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding"
  private const val GCM_IV_LENGTH = 12
  private const val GCM_TAG_LENGTH = 128

  fun generateSha256(input: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(input.toByteArray())
      .joinToString("") { "%02x".format(it) }
  }
}
    """.trimIndent(),

    "BiometricHelper.kt" to """
package com.example.data

object BiometricHelper {
  const val DEFAULT_PIN = "1234"
  const val DECOY_PIN = "9999"

  fun verifyPin(pin: String): AuthResult {
    return when (pin) {
      DEFAULT_PIN -> AuthResult.SUCCESS
      DECOY_PIN -> AuthResult.DECOY_SUCCESS
      else -> AuthResult.FAILED
    }
  }
}
    """.trimIndent(),

    "VaultScreen.kt" to """
// Biometric lock gate with ultrasonic fingerprint & 4-digit keypad
@Composable
fun VaultScreen(...) {
  if (!isUnlocked) {
    VaultLockGate(
      pinInput = pinInput,
      onUnlockBiometric = onUnlockBiometric
    )
  } else {
    VaultGalleryContent(
      selectedAlbum = selectedAlbum,
      vaultItems = vaultItems
    )
  }
}
    """.trimIndent(),

    "ReelsScreen.kt" to """
// Fullscreen vertical scrolling reels with ExoPlayer semantics
@Composable
fun ReelsScreen(...) {
  VerticalPager(state = pagerState) { page ->
    ReelItem(
      reel = reels[page],
      onToggleLike = onToggleLike,
      onOpenComments = onOpenComments
    )
  }
}
    """.trimIndent()
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.95f))
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
          .padding(16.dp),
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
              imageVector = Icons.Default.Code,
              contentDescription = "Code Hub",
              tint = CyanAccent,
              modifier = Modifier.size(22.dp)
            )
            Column {
              Text(
                text = "Kotlin Hub & CI/CD Inspector",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "GitHub Actions: actions/upload-artifact@v4 Ready",
                fontSize = 11.sp,
                color = EmeraldPrimary,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        // File Selector Tabs
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(files) { file ->
            val isSelected = file == selectedFile
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) CyanAccent else ObsidianSurfaceVariant)
                .clickable { selectedFile = file }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = file,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.Black else TextSecondary,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }

        // Code Viewer Card
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF07090D)),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        ) {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(14.dp)
          ) {
            item {
              Text(
                text = codeSnippets[selectedFile] ?: "// No content",
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFF9ECE6A)
              )
            }
          }
        }

        // Bottom status footer
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
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Valid",
              tint = EmeraldPrimary,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Gradle Wrapper & v4 Actions verified",
              fontSize = 11.sp,
              color = EmeraldPrimary,
              fontWeight = FontWeight.Medium
            )
          }

          Text(
            text = "ZERO v1.0",
            fontSize = 11.sp,
            color = TextMuted,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }
  }
}
