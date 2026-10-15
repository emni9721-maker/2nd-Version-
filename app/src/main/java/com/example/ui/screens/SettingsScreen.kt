package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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

@Composable
fun SettingsScreen(
  isAmoledMode: Boolean,
  storageCategories: List<StorageCategory>,
  cloudPlans: List<CloudPlan>,
  snapshots: List<BackupSnapshot>,
  onToggleAmoled: () -> Unit,
  onSelectPlan: (String) -> Unit,
  onCreateSnapshot: () -> Unit,
  onRestoreSnapshot: (BackupSnapshot) -> Unit,
  onOpenKotlinHub: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Top Title
    item {
      Column {
        Text(
          text = "Vault & Storage Settings",
          fontSize = 22.sp,
          fontWeight = FontWeight.ExtraBold,
          color = TextPrimary
        )
        Text(
          text = "Hardware Keystore, Cloud Tiers & Diagnostics",
          fontSize = 12.sp,
          color = TextSecondary
        )
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

    item {
      Spacer(modifier = Modifier.height(70.dp))
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
