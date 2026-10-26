package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.RoseDanger
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BackupSnapshot
import com.example.data.CloudPlan
import com.example.data.StorageCategory
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.StorageBackups
import com.example.ui.theme.StorageDocs
import com.example.ui.theme.StoragePhotos
import com.example.ui.theme.StorageVideos
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.components.SettingsModalType
import com.example.ui.components.SettingsSerialPreferencesCard
import com.example.ui.components.SavedModal
import com.example.ui.components.ArchivedModal
import com.example.ui.components.TimeManagementModal
import com.example.ui.components.BlockedAccountsModal
import com.example.ui.components.WhoCanSeeStoryModal
import com.example.ui.components.HelpModal
import com.example.ui.components.PrivacyCenterModal

@Composable
fun SettingsScreen(
  isAmoledMode: Boolean,
  storageCategories: List<StorageCategory>,
  cloudPlans: List<CloudPlan>,
  snapshots: List<BackupSnapshot>,
  // Google Photos-style Automatic Backup parameters
  isAutoBackupEnabled: Boolean = true,
  backupStatus: String = "All photos & videos backed up",
  lastBackupTime: String = "Just now",
  backupQuality: String = "Original Quality",
  backupWifiOnly: Boolean = true,
  backupFolders: List<String> = listOf("Camera", "Screenshots", "Downloads"),
  isBackingUp: Boolean = false,
  backupProgress: Float = 1.0f,
  onToggleAutoBackup: () -> Unit = {},
  onTriggerBackupNow: () -> Unit = {},
  onSetBackupQuality: (String) -> Unit = {},
  onToggleBackupWifiOnly: () -> Unit = {},
  onToggleBackupFolder: (String) -> Unit = {},
  onOpenCloudStorageView: () -> Unit = {},
  // Hardware-Encrypted Vault access (ONLY in settings)
  onOpenVault: () -> Unit,
  onToggleAmoled: () -> Unit,
  onSelectPlan: (String) -> Unit,
  onCreateSnapshot: () -> Unit,
  onRestoreSnapshot: (BackupSnapshot) -> Unit,
  onOpenKotlinHub: () -> Unit,
  onBackToFeed: (() -> Unit)? = null,
  // Language & Security parameters
  currentLanguage: String = "English",
  onSelectLanguage: (String) -> Unit = {},
  userPhone: String = "+880 1712-345678",
  userPrimaryEmail: String = "emni9721@gmail.com",
  userSecondaryEmail: String = "recovery@zero.io",
  onUpdatePhone: (String) -> Unit = {},
  onRemovePhone: () -> Unit = {},
  onUpdatePrimaryEmail: (String) -> Unit = {},
  onUpdateSecondaryEmail: (String) -> Unit = {},
  onRemoveSecondaryEmail: () -> Unit = {},
  onChangePassword: (currentPass: String, newPass: String, onResult: (Boolean, String) -> Unit) -> Unit = { _, _, cb -> cb(true, "Success") },
  onSignOut: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showLanguageDialog by remember { mutableStateOf(false) }
  var showPasswordDialog by remember { mutableStateOf(false) }
  var showPhoneDialog by remember { mutableStateOf(false) }
  var showPrimaryEmailDialog by remember { mutableStateOf(false) }
  var showRecoveryEmailDialog by remember { mutableStateOf(false) }
  var showSignOutConfirmDialog by remember { mutableStateOf(false) }
  var isSecurityExpanded by remember { mutableStateOf(false) }
  var activeDetailModal by remember { mutableStateOf<SettingsModalType?>(null) }

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      // Top Title
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (onBackToFeed != null) {
            IconButton(
              onClick = onBackToFeed,
              modifier = Modifier.testTag("settings_back_to_feed")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Feed",
                tint = TextPrimary
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
          }

          Column {
            Text(
              text = "Vault & Storage Settings",
              fontSize = 22.sp,
              fontWeight = FontWeight.ExtraBold,
              color = TextPrimary
            )
            Text(
              text = "Hardware Keystore, Google Photos Backup & Storage",
              fontSize = 12.sp,
              color = TextSecondary
            )
          }
        }
      }

      // Language & Localization Card (Bangla, English, etc.)
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
          modifier = Modifier.fillMaxWidth().testTag("language_settings_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(CyanAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Language,
                  contentDescription = "Language",
                  tint = CyanAccent,
                  modifier = Modifier.size(22.dp)
                )
              }
              Column {
                Text(
                  text = "App Language / ভাষা",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
                Text(
                  text = "Active: $currentLanguage",
                  fontSize = 12.sp,
                  color = EmeraldPrimary,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            OutlinedButton(
              onClick = { showLanguageDialog = true },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldPrimary),
              border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)),
              modifier = Modifier.testTag("change_language_button")
            ) {
              Text("Change / পরিবর্তন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Security & Account Management Card (Expandable on click: Password, Phone, Email)
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, if (isSecurityExpanded) EmeraldPrimary.copy(alpha = 0.6f) else ObsidianBorder),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { isSecurityExpanded = !isSecurityExpanded }
            .testTag("security_management_card")
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Header Row (Clickable)
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Security",
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(22.dp)
                  )
                }
                Column {
                  Text(
                    text = "Account Security & Credentials",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                  Text(
                    text = if (isSecurityExpanded) "Tap to collapse options" else "Tap to manage password, phone & email",
                    fontSize = 11.sp,
                    color = if (isSecurityExpanded) EmeraldPrimary else TextSecondary
                  )
                }
              }

              IconButton(
                onClick = { isSecurityExpanded = !isSecurityExpanded },
                modifier = Modifier.size(36.dp).testTag("toggle_security_expanded")
              ) {
                Icon(
                  imageVector = if (isSecurityExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                  contentDescription = if (isSecurityExpanded) "Collapse" else "Expand",
                  tint = if (isSecurityExpanded) EmeraldPrimary else TextSecondary,
                  modifier = Modifier.size(22.dp)
                )
              }
            }

            // Security Options (Only shown when expanded)
            AnimatedVisibility(visible = isSecurityExpanded) {
              Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

                // 1. Password Section
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    Icon(Icons.Default.Password, contentDescription = "Password", tint = CyanAccent, modifier = Modifier.size(20.dp))
                    Column {
                      Text("Master Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                      Text("Hardware-isolated AES key", fontSize = 10.sp, color = TextMuted)
                    }
                  }

                  Button(
                    onClick = { showPasswordDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                    modifier = Modifier.testTag("change_password_button")
                  ) {
                    Text("Change", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                  }
                }

                HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

                // 2. Phone Number Section
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    Icon(Icons.Default.Phone, contentDescription = "Phone", tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    Column {
                      Text("Phone Number", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                      Text(
                        text = if (userPhone.isNotBlank()) "$userPhone (Verified)" else "No phone linked",
                        fontSize = 11.sp,
                        color = if (userPhone.isNotBlank()) EmeraldPrimary else AmberWarning
                      )
                    }
                  }

                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(
                      onClick = { showPhoneDialog = true },
                      shape = RoundedCornerShape(10.dp),
                      colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceVariant),
                      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                      modifier = Modifier.testTag("manage_phone_button")
                    ) {
                      Text(if (userPhone.isNotBlank()) "Change" else "Add", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                    if (userPhone.isNotBlank()) {
                      IconButton(
                        onClick = onRemovePhone,
                        modifier = Modifier.size(34.dp).testTag("remove_phone_button")
                      ) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove Phone", tint = RoseDanger, modifier = Modifier.size(16.dp))
                      }
                    }
                  }
                }

                HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

                // 3. Email Section (Primary & Recovery)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  // Primary Email
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                      Icon(Icons.Default.Email, contentDescription = "Email", tint = CyanAccent, modifier = Modifier.size(20.dp))
                      Column {
                        Text("Primary Email", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(userPrimaryEmail, fontSize = 11.sp, color = TextSecondary)
                      }
                    }

                    Button(
                      onClick = { showPrimaryEmailDialog = true },
                      shape = RoundedCornerShape(10.dp),
                      colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceVariant),
                      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                      modifier = Modifier.testTag("change_primary_email_button")
                    ) {
                      Text("Change", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                  }

                  // Recovery Email
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                      Icon(Icons.Default.Restore, contentDescription = "Recovery Email", tint = PurpleAccent, modifier = Modifier.size(20.dp))
                      Column {
                        Text("Recovery Email", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(
                          text = if (userSecondaryEmail.isNotBlank()) userSecondaryEmail else "None configured",
                          fontSize = 11.sp,
                          color = if (userSecondaryEmail.isNotBlank()) TextSecondary else TextMuted
                        )
                      }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                      Button(
                        onClick = { showRecoveryEmailDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                        modifier = Modifier.testTag("manage_recovery_email_button")
                      ) {
                        Text(if (userSecondaryEmail.isNotBlank()) "Change" else "Add", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                      }
                      if (userSecondaryEmail.isNotBlank()) {
                        IconButton(
                          onClick = onRemoveSecondaryEmail,
                          modifier = Modifier.size(34.dp).testTag("remove_recovery_email_button")
                        ) {
                          Icon(Icons.Default.Delete, contentDescription = "Remove Recovery Email", tint = RoseDanger, modifier = Modifier.size(16.dp))
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

    // 1. Hardware-Encrypted Vault Entry Card (ONLY available here inside Settings)
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary.copy(alpha = 0.7f)),
        modifier = Modifier.testTag("encrypted_vault_settings_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(EmeraldDark.copy(alpha = 0.35f))
                  .border(1.dp, EmeraldPrimary, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Lock,
                  contentDescription = "Encrypted Vault",
                  tint = EmeraldPrimary,
                  modifier = Modifier.size(24.dp)
                )
              }

              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Hardware-Encrypted Vault",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(EmeraldPrimary.copy(alpha = 0.2f))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "PIN Locked",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = EmeraldPrimary
                    )
                  }
                }
                Text(
                  text = "AES-256 GCM MasterKey Protected",
                  fontSize = 11.sp,
                  color = CyanAccent,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Your private, decoy-protected vault is isolated strictly inside Settings. Sensitive photos, videos, and documents are encrypted with your device Keystore.",
            fontSize = 12.sp,
            color = TextSecondary,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = onOpenVault,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("enter_vault_from_settings_button")
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Open Encrypted Vault",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.Black
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    // 2. Google Photos-style Automatic Cloud Backup Section (Inside Settings)
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.testTag("google_photos_backup_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          // Card Header with Switch
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(CyanAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CloudSync,
                  contentDescription = "Cloud Backup",
                  tint = CyanAccent,
                  modifier = Modifier.size(22.dp)
                )
              }
              Column {
                Text(
                  text = "Google Photos Auto Backup",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
                Text(
                  text = if (isAutoBackupEnabled) "Automatic cloud sync active" else "Backup is paused",
                  fontSize = 11.sp,
                  color = if (isAutoBackupEnabled) EmeraldPrimary else AmberWarning
                )
              }
            }

            Switch(
              checked = isAutoBackupEnabled,
              onCheckedChange = { onToggleAutoBackup() },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = CyanAccent,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = ObsidianSurfaceVariant
              ),
              modifier = Modifier.testTag("auto_backup_switch")
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Backup Status Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.background)
              .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
              .padding(12.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = if (isAutoBackupEnabled) Icons.Default.CloudDone else Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = if (isAutoBackupEnabled) EmeraldPrimary else TextMuted,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = backupStatus,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                  )
                }

                Text(
                  text = lastBackupTime,
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }

              if (isBackingUp) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                  progress = { backupProgress },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                  color = CyanAccent,
                  trackColor = ObsidianSurfaceVariant
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Backup Now Action
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Manual Sync",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "Sync newly detected camera photos",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }

            OutlinedButton(
              onClick = onTriggerBackupNow,
              enabled = !isBackingUp && isAutoBackupEnabled,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent),
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isAutoBackupEnabled) CyanAccent else ObsidianBorder),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("backup_now_button")
            ) {
              Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Sync",
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isBackingUp) "Backing up..." else "Back Up Now",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Backup Quality Selection
          Text(
            text = "Backup Quality",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf("Original Quality", "Storage Saver").forEach { quality ->
              val isSelected = backupQuality == quality
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) CyanAccent.copy(alpha = 0.15f) else MaterialTheme.colorScheme.background)
                  .border(
                    width = 1.dp,
                    color = if (isSelected) CyanAccent else ObsidianBorder,
                    shape = RoundedCornerShape(10.dp)
                  )
                  .clickable { onSetBackupQuality(quality) }
                  .padding(vertical = 10.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = quality,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) CyanAccent else TextPrimary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Wi-Fi Only Toggle
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(MaterialTheme.colorScheme.background)
              .border(1.dp, ObsidianBorder, RoundedCornerShape(10.dp))
              .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = null,
                tint = CyanAccent,
                modifier = Modifier.size(18.dp)
              )
              Column {
                Text(
                  text = "Back up over Wi-Fi only",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium,
                  color = TextPrimary
                )
                Text(
                  text = "Prevent mobile data consumption",
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }
            }

            Switch(
              checked = backupWifiOnly,
              onCheckedChange = { onToggleBackupWifiOnly() },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = CyanAccent,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = ObsidianSurfaceVariant
              )
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Device Folders
          Text(
            text = "Back Up Device Folders",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Spacer(modifier = Modifier.height(6.dp))

          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            listOf("Camera", "Screenshots", "Downloads").forEach { folder ->
              val isIncluded = backupFolders.contains(folder)
              FilterChip(
                selected = isIncluded,
                onClick = { onToggleBackupFolder(folder) },
                label = {
                  Text(
                    text = folder,
                    fontSize = 11.sp,
                    color = if (isIncluded) Color.Black else TextPrimary
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
                  selected = isIncluded
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "ℹ️ Photos & videos backed up here are accessible in your cloud library. Hardware-encrypted Vault items are kept separate and private.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 15.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Button to view all uploaded media in automatic backup storage
          Button(
            onClick = onOpenCloudStorageView,
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("cloud_storage_view_button")
          ) {
            Icon(
              imageVector = Icons.Default.CloudDone,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Cloud Storage View",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.Black
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    // Theme Switch Card: Midnight Obsidian vs AMOLED Pure Black
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(EmeraldDark.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.DarkMode,
                contentDescription = "Theme",
                tint = EmeraldPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Text(
                text = "AMOLED Pure Black Mode",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = if (isAmoledMode) "Active: #000000 true black (Max OLED battery savings)" else "Active: Obsidian Dark (#0C0E12)",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }
          }

          Switch(
            checked = isAmoledMode,
            onCheckedChange = { onToggleAmoled() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.Black,
              checkedTrackColor = EmeraldPrimary,
              uncheckedThumbColor = TextMuted,
              uncheckedTrackColor = ObsidianSurfaceVariant
            ),
            modifier = Modifier.testTag("amoled_switch")
          )
        }
      }
    }

    // Storage Analytics Breakdown (Donut Chart & Progress Bars)
    item {
      StorageBreakdownCard(categories = storageCategories)
    }

    // Cloud Storage Plans (Starter to Infinite Enterprise)
    item {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          text = "Cloud Vault Plans",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )

        cloudPlans.forEach { plan ->
          CloudPlanCard(
            plan = plan,
            onSelect = { onSelectPlan(plan.id) }
          )
        }
      }
    }

    // Automated Database Snapshots
    item {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "AES-256 Backup Snapshots",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Instant 1-click cloud recovery snapshots",
              fontSize = 11.sp,
              color = TextSecondary
            )
          }

          Button(
            onClick = onCreateSnapshot,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.testTag("create_snapshot_button")
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "New Snapshot",
              tint = Color.Black,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Snapshot", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          }
        }

        snapshots.forEach { snap ->
          SnapshotItemCard(
            snapshot = snap,
            onRestore = { onRestoreSnapshot(snap) }
          )
        }
      }
    }

    // Native Android Architecture & GitHub Actions Status
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceVariant),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Android,
              contentDescription = "Native Android",
              tint = CyanAccent,
              modifier = Modifier.size(24.dp)
            )
            Column {
              Text(
                text = "Native Android SDK & CI/CD",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "GitHub Actions Workflow configured (actions/upload-artifact@v4)",
                fontSize = 11.sp,
                color = CyanAccent
              )
            }
          }

          Text(
            text = "Automated release pipeline configured in .github/workflows/main.yml. Ready to compile native debug & release APKs with Gradle wrapper support.",
            fontSize = 12.sp,
            color = TextSecondary,
            lineHeight = 16.sp
          )

          Button(
            onClick = onOpenKotlinHub,
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("open_kotlin_hub_button")
          ) {
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = "Explore Code",
              tint = Color.Black,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Inspect Kotlin Code & CI/CD Config", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          }
        }
      }
    }

    // =========================================================================
    // SECTION 6.5: SOCIAL & PRIVACY PREFERENCES (Saved, Archived, Time managments, Blocked Account, Who can see story, Help, Privacy Center)
    // =========================================================================
    item {
      SettingsSerialPreferencesCard(
        onSelectOption = { modalType ->
          activeDetailModal = modalType
        }
      )
    }

    // =========================================================================
    // SECTION 7: ACCOUNT SESSION & SIGN OUT (Located at the very bottom of Settings)
    // =========================================================================
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, RoseDanger.copy(alpha = 0.35f)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("account_session_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(RoseDanger.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.Logout,
                  contentDescription = "Sign Out",
                  tint = RoseDanger,
                  modifier = Modifier.size(20.dp)
                )
              }
              Column {
                Text(
                  text = "Account Session / সাইন আউট",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
                Text(
                  text = "Terminate local encryption session",
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }
            }
          }

          Text(
            text = "Signing out clears active decryption keys from local memory. You will need to re-authenticate with your mobile number, email, or username and password to regain access.",
            fontSize = 12.sp,
            color = TextSecondary,
            lineHeight = 16.sp
          )

          Button(
            onClick = { showSignOutConfirmDialog = true },
            colors = ButtonDefaults.buttonColors(
              containerColor = RoseDanger,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("sign_out_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Logout,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Sign Out of ZERO",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(70.dp))
    }
  }

  // Sign Out Confirmation Overlay (In-tree overlay prevents SurfaceSyncGroup window timeout)
  if (showSignOutConfirmDialog) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.75f))
        .clickable(enabled = true, onClick = { showSignOutConfirmDialog = false })
        .padding(24.dp)
        .testTag("sign_out_confirm_overlay"),
      contentAlignment = Alignment.Center
    ) {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier
          .fillMaxWidth()
          .clickable(enabled = true, onClick = { /* consume click */ })
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Logout,
              contentDescription = null,
              tint = RoseDanger,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "Sign Out Confirmation",
              color = TextPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            )
          }

          Text(
            text = "Are you sure you want to sign out? Your hardware-encrypted vault and active cloud synchronization will be safely locked until you sign in again.",
            fontSize = 13.sp,
            color = TextSecondary,
            lineHeight = 18.sp
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { showSignOutConfirmDialog = false },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
              border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
              modifier = Modifier.weight(1f)
            ) {
              Text("Cancel")
            }

            Button(
              onClick = {
                showSignOutConfirmDialog = false
                onSignOut()
              },
              colors = ButtonDefaults.buttonColors(containerColor = RoseDanger, contentColor = Color.White),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("confirm_sign_out_button")
            ) {
              Text("Sign Out", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
  if (showLanguageDialog) {
    LanguageSelectionDialog(
      currentLanguage = currentLanguage,
      onSelectLanguage = {
        onSelectLanguage(it)
        showLanguageDialog = false
      },
      onDismiss = { showLanguageDialog = false }
    )
  }

  if (showPasswordDialog) {
    ChangePasswordDialog(
      onSubmit = { cur, new, cb ->
        onChangePassword(cur, new) { success, msg ->
          cb(success, msg)
          if (success) {
            showPasswordDialog = false
          }
        }
      },
      onDismiss = { showPasswordDialog = false }
    )
  }

  if (showPhoneDialog) {
    PhoneManagementDialog(
      initialPhone = userPhone,
      onSave = {
        onUpdatePhone(it)
        showPhoneDialog = false
      },
      onDismiss = { showPhoneDialog = false }
    )
  }

  if (showPrimaryEmailDialog) {
    EmailManagementDialog(
      title = "Change Primary Email / প্রাইমারি ইমেইল",
      initialEmail = userPrimaryEmail,
      isPrimary = true,
      onSave = {
        onUpdatePrimaryEmail(it)
        showPrimaryEmailDialog = false
      },
      onDismiss = { showPrimaryEmailDialog = false }
    )
  }

  if (showRecoveryEmailDialog) {
    EmailManagementDialog(
      title = "Recovery Email / রিকভারি ইমেইল",
      initialEmail = userSecondaryEmail,
      isPrimary = false,
      onSave = {
        onUpdateSecondaryEmail(it)
        showRecoveryEmailDialog = false
      },
      onDismiss = { showRecoveryEmailDialog = false }
    )
  }

  // In-tree Overlay Modals for the 7 preferences (Saved, Archived, Time managments, Blocked Account, Who can see story, Help, Privacy Center)
  when (activeDetailModal) {
    SettingsModalType.SAVED -> SavedModal(onDismiss = { activeDetailModal = null })
    SettingsModalType.ARCHIVED -> ArchivedModal(onDismiss = { activeDetailModal = null })
    SettingsModalType.TIME_MANAGEMENT -> TimeManagementModal(onDismiss = { activeDetailModal = null })
    SettingsModalType.BLOCKED_ACCOUNTS -> BlockedAccountsModal(onDismiss = { activeDetailModal = null })
    SettingsModalType.STORY_PRIVACY -> WhoCanSeeStoryModal(onDismiss = { activeDetailModal = null })
    SettingsModalType.HELP -> HelpModal(onDismiss = { activeDetailModal = null })
    SettingsModalType.PRIVACY_CENTER -> PrivacyCenterModal(onDismiss = { activeDetailModal = null })
    null -> {}
  }
  }
}

@Composable
private fun StorageBreakdownCard(categories: List<StorageCategory>) {
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
  ) {
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
        Column {
          Text(
            text = "Storage Analytics",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "18.4 GB Used of 50.0 GB Total Quota",
            fontSize = 12.sp,
            color = CyanAccent,
            fontWeight = FontWeight.SemiBold
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(EmeraldDark.copy(alpha = 0.3f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text("31.6 GB FREE", fontSize = 10.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
        }
      }

      // Donut Chart + Legend
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Custom Canvas Donut
        Box(
          modifier = Modifier.size(110.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 22f
            var startAngle = -90f
            val colors = listOf(StoragePhotos, StorageVideos, StorageDocs, StorageBackups)

            categories.forEachIndexed { index, cat ->
              val sweep = cat.percentage * 360f
              drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweep - 2f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
              )
              startAngle += sweep
            }
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("36.8%", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
            Text("USED", fontSize = 9.sp, color = TextMuted)
          }
        }

        // Granular Legend List
        Column(
          modifier = Modifier
            .weight(1f)
            .padding(start = 16.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val colors = listOf(StoragePhotos, StorageVideos, StorageDocs, StorageBackups)
          categories.forEachIndexed { index, cat ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colors[index % colors.size])
                )
                Text(
                  text = cat.name.split(" ").first(),
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }

              Text(
                text = "${cat.sizeGb} GB",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun CloudPlanCard(
  plan: CloudPlan,
  onSelect: () -> Unit
) {
  val isCurrent = plan.isCurrent
  Card(
    colors = CardDefaults.cardColors(
      containerColor = if (isCurrent) ObsidianSurfaceElevated else MaterialTheme.colorScheme.surface
    ),
    shape = RoundedCornerShape(14.dp),
    border = androidx.compose.foundation.BorderStroke(
      if (isCurrent) 1.5.dp else 1.dp,
      if (isCurrent) EmeraldPrimary else ObsidianBorder
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onSelect)
      .testTag("plan_card_${plan.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = plan.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          if (plan.badge != null) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isCurrent) EmeraldPrimary else CyanAccent)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = plan.badge,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
              )
            }
          }
        }

        Text(
          text = plan.priceMonthly,
          fontSize = 15.sp,
          fontWeight = FontWeight.ExtraBold,
          color = if (isCurrent) EmeraldPrimary else CyanAccent
        )
      }

      Text(
        text = "${plan.capacityGb} GB Total Capacity",
        fontSize = 12.sp,
        color = TextSecondary
      )

      Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        plan.features.take(3).forEach { feature ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "Included",
              tint = EmeraldPrimary,
              modifier = Modifier.size(12.dp)
            )
            Text(
              text = feature,
              fontSize = 11.sp,
              color = TextMuted
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SnapshotItemCard(
  snapshot: BackupSnapshot,
  onRestore: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surface)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
      .padding(12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = snapshot.name,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )
      Text(
        text = "${snapshot.dateFormatted} • ${snapshot.sizeMb} MB",
        fontSize = 11.sp,
        color = TextSecondary
      )
      Text(
        text = "SHA-256: ${snapshot.hash}",
        fontSize = 9.sp,
        color = CyanAccent
      )
    }

    OutlinedButton(
      onClick = onRestore,
      colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldPrimary),
      border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(14.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Restore,
        contentDescription = "Restore",
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text("Restore", fontSize = 11.sp)
    }
  }
}

@Composable
private fun LanguageSelectionDialog(
  currentLanguage: String,
  onSelectLanguage: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val languages = listOf(
    "Bangla (বাংলা)" to "bn",
    "English (US)" to "en",
    "Español (Spanish)" to "es",
    "हिन्दी (Hindi)" to "hi",
    "العربية (Arabic)" to "ar",
    "Français (French)" to "fr",
    "Deutsch (German)" to "de"
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      shape = RoundedCornerShape(20.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("language_selection_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
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
                .size(36.dp)
                .clip(CircleShape)
                .background(CyanAccent.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Language, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            }
            Column {
              Text("Select Language", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
              Text("ভাষা নির্বাচন করুন", fontSize = 11.sp, color = TextSecondary)
            }
          }
        }

        HorizontalDivider(color = ObsidianBorder)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          languages.forEach { (langName, _) ->
            val isSelected = currentLanguage.contains(langName.substringBefore(" ")) ||
                             langName.contains(currentLanguage, ignoreCase = true)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) EmeraldDark.copy(alpha = 0.35f) else ObsidianSurfaceVariant)
                .border(1.dp, if (isSelected) EmeraldPrimary else ObsidianBorder, RoundedCornerShape(12.dp))
                .clickable { onSelectLanguage(langName) }
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .testTag("lang_option_${langName.take(5)}"),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = langName,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) EmeraldPrimary else TextPrimary
              )
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Selected",
                  tint = EmeraldPrimary,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }

        Button(
          onClick = onDismiss,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceVariant),
          border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
          modifier = Modifier.fillMaxWidth().testTag("close_language_dialog_button")
        ) {
          Text("Cancel / বাতিল", fontSize = 12.sp, color = TextPrimary)
        }
      }
    }
  }
}

@Composable
private fun ChangePasswordDialog(
  onSubmit: (currentPass: String, newPass: String, onResult: (Boolean, String) -> Unit) -> Unit,
  onDismiss: () -> Unit
) {
  var currentPassword by remember { mutableStateOf("") }
  var newPassword by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  var isCurrentVisible by remember { mutableStateOf(false) }
  var isNewVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isSubmitting by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      shape = RoundedCornerShape(20.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("change_password_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(CyanAccent.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Password, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
          }
          Column {
            Text("Change Password", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("পাসওয়ার্ড পরিবর্তন", fontSize = 11.sp, color = TextSecondary)
          }
        }

        HorizontalDivider(color = ObsidianBorder)

        if (errorMessage != null) {
          Text(
            text = errorMessage ?: "",
            color = RoseDanger,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        OutlinedTextField(
          value = currentPassword,
          onValueChange = {
            currentPassword = it
            errorMessage = null
          },
          label = { Text("Current Password", fontSize = 12.sp, color = TextMuted) },
          visualTransformation = if (isCurrentVisible) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { isCurrentVisible = !isCurrentVisible }) {
              Icon(
                imageVector = if (isCurrentVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
              )
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyanAccent,
            unfocusedBorderColor = ObsidianBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("current_password_input")
        )

        OutlinedTextField(
          value = newPassword,
          onValueChange = {
            newPassword = it
            errorMessage = null
          },
          label = { Text("New Password (min 6 chars)", fontSize = 12.sp, color = TextMuted) },
          visualTransformation = if (isNewVisible) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { isNewVisible = !isNewVisible }) {
              Icon(
                imageVector = if (isNewVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
              )
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyanAccent,
            unfocusedBorderColor = ObsidianBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("new_password_input")
        )

        OutlinedTextField(
          value = confirmPassword,
          onValueChange = {
            confirmPassword = it
            errorMessage = null
          },
          label = { Text("Confirm New Password", fontSize = 12.sp, color = TextMuted) },
          visualTransformation = PasswordVisualTransformation(),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyanAccent,
            unfocusedBorderColor = ObsidianBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("confirm_password_input")
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
            modifier = Modifier.weight(1f).testTag("cancel_password_button")
          ) {
            Text("Cancel", fontSize = 12.sp)
          }

          Button(
            onClick = {
              if (newPassword.length < 6) {
                errorMessage = "New password must be at least 6 characters"
                return@Button
              }
              if (newPassword != confirmPassword) {
                errorMessage = "Passwords do not match"
                return@Button
              }
              isSubmitting = true
              onSubmit(currentPassword, newPassword) { success, msg ->
                isSubmitting = false
                if (!success) {
                  errorMessage = msg
                }
              }
            },
            enabled = !isSubmitting,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
            modifier = Modifier.weight(1f).testTag("save_password_button")
          ) {
            Text("Save", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          }
        }
      }
    }
  }
}

@Composable
private fun PhoneManagementDialog(
  initialPhone: String,
  onSave: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var phoneText by remember { mutableStateOf(initialPhone) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      shape = RoundedCornerShape(20.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("manage_phone_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Phone, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
          }
          Column {
            Text("Phone Number", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("ফোন নম্বর যুক্ত / পরিবর্তন", fontSize = 11.sp, color = TextSecondary)
          }
        }

        HorizontalDivider(color = ObsidianBorder)

        if (errorMessage != null) {
          Text(text = errorMessage ?: "", color = RoseDanger, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        OutlinedTextField(
          value = phoneText,
          onValueChange = {
            phoneText = it
            errorMessage = null
          },
          label = { Text("Phone Number", fontSize = 12.sp, color = TextMuted) },
          placeholder = { Text("+880 1712-345678", fontSize = 12.sp, color = TextMuted) },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EmeraldPrimary,
            unfocusedBorderColor = ObsidianBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("dialog_phone_input")
        )

        Text(
          text = "Phone number is encrypted using your device MasterKey for 2FA and account restoration.",
          fontSize = 11.sp,
          color = TextSecondary,
          lineHeight = 15.sp
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
            modifier = Modifier.weight(1f).testTag("cancel_phone_dialog_button")
          ) {
            Text("Cancel", fontSize = 12.sp)
          }

          Button(
            onClick = {
              if (phoneText.isBlank()) {
                errorMessage = "Please enter a valid phone number"
                return@Button
              }
              onSave(phoneText.trim())
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier.weight(1f).testTag("save_phone_dialog_button")
          ) {
            Text("Save", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          }
        }
      }
    }
  }
}

@Composable
private fun EmailManagementDialog(
  title: String,
  initialEmail: String,
  isPrimary: Boolean,
  onSave: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var emailText by remember { mutableStateOf(initialEmail) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      shape = RoundedCornerShape(20.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("manage_email_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(if (isPrimary) CyanAccent.copy(alpha = 0.2f) else PurpleAccent.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isPrimary) Icons.Default.Email else Icons.Default.Restore,
              contentDescription = null,
              tint = if (isPrimary) CyanAccent else PurpleAccent,
              modifier = Modifier.size(20.dp)
            )
          }
          Column {
            Text(title.substringBefore("/").trim(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(title.substringAfter("/", "").trim(), fontSize = 11.sp, color = TextSecondary)
          }
        }

        HorizontalDivider(color = ObsidianBorder)

        if (errorMessage != null) {
          Text(text = errorMessage ?: "", color = RoseDanger, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        OutlinedTextField(
          value = emailText,
          onValueChange = {
            emailText = it
            errorMessage = null
          },
          label = { Text("Email Address", fontSize = 12.sp, color = TextMuted) },
          placeholder = { Text("user@example.com", fontSize = 12.sp, color = TextMuted) },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isPrimary) CyanAccent else PurpleAccent,
            unfocusedBorderColor = ObsidianBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("dialog_email_input")
        )

        Text(
          text = if (isPrimary) {
            "Primary email is used for cloud synchronization and active notifications."
          } else {
            "Recovery email helps restore vault keys if you lose access to your primary credentials."
          },
          fontSize = 11.sp,
          color = TextSecondary,
          lineHeight = 15.sp
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
            modifier = Modifier.weight(1f).testTag("cancel_email_dialog_button")
          ) {
            Text("Cancel", fontSize = 12.sp)
          }

          Button(
            onClick = {
              if (emailText.isBlank() || !emailText.contains("@")) {
                errorMessage = "Please enter a valid email address"
                return@Button
              }
              onSave(emailText.trim())
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isPrimary) CyanAccent else PurpleAccent),
            modifier = Modifier.weight(1f).testTag("save_email_dialog_button")
          ) {
            Text("Save", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          }
        }
      }
    }
  }
}

