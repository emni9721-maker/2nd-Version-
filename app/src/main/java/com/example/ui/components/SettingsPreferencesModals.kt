package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class SettingsModalType {
  SAVED,
  ARCHIVED,
  TIME_MANAGEMENT,
  BLOCKED_ACCOUNTS,
  STORY_PRIVACY,
  HELP,
  PRIVACY_CENTER
}

/**
 * Settings Card that lists all 7 requested options serially:
 * 1. Saved
 * 2. Archived
 * 3. Time managments
 * 4. Blocked Account
 * 5. Who can see story
 * 6. Help
 * 7. Privacy Center
 */
@Composable
fun SettingsSerialPreferencesCard(
  onSelectOption: (SettingsModalType) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
    modifier = modifier
      .fillMaxWidth()
      .testTag("serial_preferences_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
        text = "Social & Privacy Preferences",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )
      Text(
        text = "Manage your activity, privacy and digital well-being",
        fontSize = 11.sp,
        color = TextSecondary
      )

      Spacer(modifier = Modifier.height(6.dp))
      HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

      // 1. Saved
      SettingsSerialRowItem(
        title = "Saved",
        subtitle = "Posts, reels & media you saved for later",
        icon = Icons.Default.Bookmark,
        iconTint = CyanAccent,
        testTag = "settings_option_saved",
        onClick = { onSelectOption(SettingsModalType.SAVED) }
      )

      HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.3f))

      // 2. Archived
      SettingsSerialRowItem(
        title = "Archived",
        subtitle = "Expired stories & hidden profile posts",
        icon = Icons.Default.Archive,
        iconTint = EmeraldPrimary,
        testTag = "settings_option_archived",
        onClick = { onSelectOption(SettingsModalType.ARCHIVED) }
      )

      HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.3f))

      // 3. Time managments
      SettingsSerialRowItem(
        title = "Time managments",
        subtitle = "Daily screen time & break reminders",
        icon = Icons.Default.Timer,
        iconTint = PurpleAccent,
        testTag = "settings_option_time_management",
        onClick = { onSelectOption(SettingsModalType.TIME_MANAGEMENT) }
      )

      HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.3f))

      // 4. Blocked Account
      SettingsSerialRowItem(
        title = "Blocked Account",
        subtitle = "Manage restricted and blocked profiles",
        icon = Icons.Default.Block,
        iconTint = RoseDanger,
        testTag = "settings_option_blocked_account",
        onClick = { onSelectOption(SettingsModalType.BLOCKED_ACCOUNTS) }
      )

      HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.3f))

      // 5. Who can see story
      SettingsSerialRowItem(
        title = "Who can see story",
        subtitle = "Story audience, friends & visibility rules",
        icon = Icons.Default.Visibility,
        iconTint = AmberWarning,
        testTag = "settings_option_who_can_see_story",
        onClick = { onSelectOption(SettingsModalType.STORY_PRIVACY) }
      )

      HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.3f))

      // 6. Help
      SettingsSerialRowItem(
        title = "Help",
        subtitle = "Help Center, report problem & safety guides",
        icon = Icons.AutoMirrored.Filled.Help,
        iconTint = EmeraldPrimary,
        testTag = "settings_option_help",
        onClick = { onSelectOption(SettingsModalType.HELP) }
      )

      HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.3f))

      // 7. Privacy Center
      SettingsSerialRowItem(
        title = "Privacy Center",
        subtitle = "Zero-Knowledge encryption & data audits",
        icon = Icons.Default.PrivacyTip,
        iconTint = CyanAccent,
        testTag = "settings_option_privacy_center",
        onClick = { onSelectOption(SettingsModalType.PRIVACY_CENTER) }
      )
    }
  }
}

@Composable
private fun SettingsSerialRowItem(
  title: String,
  subtitle: String,
  icon: ImageVector,
  iconTint: Color,
  testTag: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .clickable(onClick = onClick)
      .padding(vertical = 10.dp, horizontal = 4.dp)
      .testTag(testTag),
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
          .size(38.dp)
          .clip(CircleShape)
          .background(iconTint.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = iconTint,
          modifier = Modifier.size(20.dp)
        )
      }
      Column {
        Text(
          text = title,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = TextSecondary
        )
      }
    }

    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForward,
      contentDescription = "Open",
      tint = TextMuted,
      modifier = Modifier.size(16.dp)
    )
  }
}

// =========================================================================
// 1. Saved Modal
// =========================================================================
@Composable
fun SavedModal(onDismiss: () -> Unit) {
  BackHandler { onDismiss() }

  data class SavedItem(val id: String, val title: String, val author: String, val time: String, val imageUrl: String)
  val savedItems = remember {
    mutableStateListOf(
      SavedItem("1", "Cyberpunk Nightscape 4K HDR", "@elena_sec", "Saved 2 days ago", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=300&q=80"),
      SavedItem("2", "Hardware Keystore Whitepaper", "@zero_labs", "Saved 5 days ago", "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=300&q=80"),
      SavedItem("3", "Cinematic City Lights Reel", "@cyber_nomad", "Saved 1 week ago", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=300&q=80")
    )
  }

  ModalContainer(title = "Saved", subtitle = "সংরক্ষিত পোস্ট ও কনটেন্ট", icon = Icons.Default.Bookmark, iconTint = CyanAccent, onDismiss = onDismiss) {
    if (savedItems.isEmpty()) {
      Text("No saved items yet.", color = TextMuted, fontSize = 13.sp, modifier = Modifier.padding(vertical = 24.dp))
    } else {
      savedItems.forEach { item ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurfaceVariant)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(54.dp)
              .clip(RoundedCornerShape(8.dp))
          )
          Column(modifier = Modifier.weight(1f)) {
            Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("${item.author} • ${item.time}", fontSize = 11.sp, color = TextSecondary)
          }
          IconButton(
            onClick = { savedItems.remove(item) },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Default.BookmarkBorder, contentDescription = "Unsave", tint = RoseDanger, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}

// =========================================================================
// 2. Archived Modal
// =========================================================================
@Composable
fun ArchivedModal(onDismiss: () -> Unit) {
  BackHandler { onDismiss() }

  data class ArchiveItem(val id: String, val title: String, val date: String, val type: String)
  val archiveItems = remember {
    mutableStateListOf(
      ArchiveItem("a1", "Late night coding session", "Aug 14, 2026", "Story Archive"),
      ArchiveItem("a2", "Hardware Keystore Initial Test", "Jul 22, 2026", "Post Archive"),
      ArchiveItem("a3", "Dhaka Cyber Skyline Capture", "Jun 10, 2026", "Story Archive")
    )
  }

  ModalContainer(title = "Archived", subtitle = "আর্কাইভ করা স্টোরি ও পোস্ট", icon = Icons.Default.Archive, iconTint = EmeraldPrimary, onDismiss = onDismiss) {
    Text(
      text = "Only you can see items in your archive unless you choose to restore or share them.",
      fontSize = 11.sp,
      color = TextSecondary
    )

    archiveItems.forEach { item ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ObsidianSurfaceVariant)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          Text("${item.type} • Expired ${item.date}", fontSize = 11.sp, color = TextSecondary)
        }

        OutlinedButton(
          onClick = { archiveItems.remove(item) },
          colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldPrimary),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Restore", fontSize = 11.sp)
        }
      }
    }
  }
}

// =========================================================================
// 3. Time Management Modal
// =========================================================================
@Composable
fun TimeManagementModal(onDismiss: () -> Unit) {
  BackHandler { onDismiss() }

  var dailyLimitMinutes by remember { mutableFloatStateOf(60f) }
  var isDailyReminderEnabled by remember { mutableStateOf(true) }
  var isBreakReminderEnabled by remember { mutableStateOf(true) }

  ModalContainer(title = "Time managments", subtitle = "দৈনিক ব্যবহারের সময় ও রিমাইন্ডার", icon = Icons.Default.Timer, iconTint = PurpleAccent, onDismiss = onDismiss) {
    // Usage Summary Card
    Card(
      colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceElevated),
      shape = RoundedCornerShape(14.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Today's Screen Time", fontSize = 12.sp, color = TextSecondary)
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Text("42", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = PurpleAccent)
          Text("mins", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, modifier = Modifier.padding(bottom = 4.dp))
        }
        Text("Daily average: 38 mins (12% lower than last week)", fontSize = 11.sp, color = EmeraldPrimary)
      }
    }

    // Daily Reminder Toggle
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text("Daily Screen Time Limit", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Get notified when you reach ${dailyLimitMinutes.toInt()} minutes", fontSize = 11.sp, color = TextSecondary)
      }
      Switch(
        checked = isDailyReminderEnabled,
        onCheckedChange = { isDailyReminderEnabled = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = PurpleAccent)
      )
    }

    if (isDailyReminderEnabled) {
      Column {
        Text("Set Limit: ${dailyLimitMinutes.toInt()} minutes", fontSize = 12.sp, color = PurpleAccent, fontWeight = FontWeight.Bold)
        Slider(
          value = dailyLimitMinutes,
          onValueChange = { dailyLimitMinutes = it },
          valueRange = 15f..180f,
          steps = 10,
          colors = SliderDefaults.colors(thumbColor = PurpleAccent, activeTrackColor = PurpleAccent)
        )
      }
    }

    HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

    // Break Reminder Toggle
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text("Take a Break Reminder", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Remind to rest your eyes every 30 minutes of continuous use", fontSize = 11.sp, color = TextSecondary)
      }
      Switch(
        checked = isBreakReminderEnabled,
        onCheckedChange = { isBreakReminderEnabled = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = EmeraldPrimary)
      )
    }
  }
}

// =========================================================================
// 4. Blocked Accounts Modal
// =========================================================================
@Composable
fun BlockedAccountsModal(onDismiss: () -> Unit) {
  BackHandler { onDismiss() }

  data class BlockedUser(val id: String, val username: String, val date: String)
  val blockedList = remember {
    mutableStateListOf(
      BlockedUser("b1", "@spammer_bot_91", "Blocked on Oct 12"),
      BlockedUser("b2", "@anonymous_tracker", "Blocked on Sep 29"),
      BlockedUser("b3", "@fake_profile_zero", "Blocked on Aug 18")
    )
  }
  var newBlockInput by remember { mutableStateOf("") }

  ModalContainer(title = "Blocked Account", subtitle = "ব্লক করা অ্যাকাউন্ট পরিচালনা", icon = Icons.Default.Block, iconTint = RoseDanger, onDismiss = onDismiss) {
    // Add block input
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = newBlockInput,
        onValueChange = { newBlockInput = it },
        placeholder = { Text("@username to block", fontSize = 12.sp, color = TextMuted) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDanger, unfocusedBorderColor = ObsidianBorder),
        modifier = Modifier.weight(1f)
      )
      Button(
        onClick = {
          if (newBlockInput.isNotBlank()) {
            blockedList.add(0, BlockedUser(System.currentTimeMillis().toString(), newBlockInput.trim(), "Just now"))
            newBlockInput = ""
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = RoseDanger),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Block", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Text("Blocked users cannot message you, see your posts, or view your vault.", fontSize = 11.sp, color = TextSecondary)

    // List of blocked users
    blockedList.forEach { user ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ObsidianSurfaceVariant)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(user.username, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          Text(user.date, fontSize = 11.sp, color = TextMuted)
        }
        OutlinedButton(
          onClick = { blockedList.remove(user) },
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
          border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Unblock", fontSize = 11.sp)
        }
      }
    }
  }
}

// =========================================================================
// 5. Who Can See Story Modal
// =========================================================================
@Composable
fun WhoCanSeeStoryModal(onDismiss: () -> Unit) {
  BackHandler { onDismiss() }

  var selectedAudience by remember { mutableStateOf("Friends Only") }
  val options = listOf(
    "Everyone" to "Anyone on ZERO can see your daily stories",
    "Friends Only" to "Only followers that you follow back",
    "Close Friends" to "Only members of your VIP Close Friends list",
    "Hide Story From..." to "Exclude specific users from seeing your stories"
  )

  ModalContainer(title = "Who can see story", subtitle = "স্টোরি প্রাইভেসি ও অডিয়েন্স", icon = Icons.Default.Visibility, iconTint = AmberWarning, onDismiss = onDismiss) {
    Text(
      text = "Changes to your story privacy apply to active and future 24-hour stories.",
      fontSize = 11.sp,
      color = TextSecondary
    )

    options.forEach { (optionTitle, optionDesc) ->
      val isSelected = selectedAudience == optionTitle
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(if (isSelected) AmberWarning.copy(alpha = 0.12f) else ObsidianSurfaceVariant)
          .border(1.dp, if (isSelected) AmberWarning else ObsidianBorder, RoundedCornerShape(12.dp))
          .clickable { selectedAudience = optionTitle }
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Icon(
          imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
          contentDescription = null,
          tint = if (isSelected) AmberWarning else TextMuted,
          modifier = Modifier.size(20.dp)
        )
        Column {
          Text(optionTitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (isSelected) AmberWarning else TextPrimary)
          Text(optionDesc, fontSize = 11.sp, color = TextSecondary)
        }
      }
    }

    Button(
      onClick = onDismiss,
      colors = ButtonDefaults.buttonColors(containerColor = AmberWarning),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Text("Save Story Privacy", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
  }
}

// =========================================================================
// 6. Help Modal
// =========================================================================
@Composable
fun HelpModal(onDismiss: () -> Unit) {
  BackHandler { onDismiss() }

  var reportSubmitted by remember { mutableStateOf(false) }
  var reportText by remember { mutableStateOf("") }

  ModalContainer(title = "Help", subtitle = "সাহায্য ও সাপোর্ট সেন্টার", icon = Icons.AutoMirrored.Filled.Help, iconTint = EmeraldPrimary, onDismiss = onDismiss) {
    Text("Frequently Asked Questions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

    FaqAccordion(
      question = "How does ZERO's AES-256 E2EE encryption work?",
      answer = "All photos, videos, and credentials are encrypted on-device before syncing to Firestore. Only your hardware Keystore holds the private decryption key."
    )
    FaqAccordion(
      question = "What is Decoy Vault mode?",
      answer = "Entering the decoy PIN (default 9999) opens an alternate vault environment with innocuous dummy files for coercive security."
    )
    FaqAccordion(
      question = "How does Google Photos Auto Backup work?",
      answer = "When enabled in Settings, all camera and selected device folders automatically sync to your personal encrypted cloud quota."
    )

    HorizontalDivider(color = ObsidianBorder)

    Text("Report a Problem", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

    if (reportSubmitted) {
      Text("✅ Thank you! Your report has been submitted to ZERO support.", color = EmeraldPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    } else {
      OutlinedTextField(
        value = reportText,
        onValueChange = { reportText = it },
        placeholder = { Text("Describe the issue or feature feedback...", fontSize = 12.sp, color = TextMuted) },
        minLines = 2,
        maxLines = 4,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldPrimary, unfocusedBorderColor = ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
      )
      Button(
        onClick = {
          if (reportText.isNotBlank()) reportSubmitted = true
        },
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("Submit Report", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
      }
    }
  }
}

@Composable
private fun FaqAccordion(question: String, answer: String) {
  var isExpanded by remember { mutableStateOf(false) }
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(ObsidianSurfaceVariant)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(10.dp))
      .clickable { isExpanded = !isExpanded }
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(question, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.weight(1f))
      Icon(
        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = null,
        tint = EmeraldPrimary,
        modifier = Modifier.size(18.dp)
      )
    }
    AnimatedVisibility(visible = isExpanded) {
      Text(answer, fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)
    }
  }
}

// =========================================================================
// 7. Privacy Center Modal
// =========================================================================
@Composable
fun PrivacyCenterModal(onDismiss: () -> Unit) {
  BackHandler { onDismiss() }

  var isPrivateSearch by remember { mutableStateOf(true) }
  var isReadReceipts by remember { mutableStateOf(false) }
  var downloadRequested by remember { mutableStateOf(false) }

  ModalContainer(title = "Privacy Center", subtitle = "জিরো নলেজ এনক্রিপশন ও প্রাইভেসি কন্ট্রোল", icon = Icons.Default.PrivacyTip, iconTint = CyanAccent, onDismiss = onDismiss) {
    // Cryptographic Status Card
    Card(
      colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceElevated),
      shape = RoundedCornerShape(14.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.3f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(Icons.Default.Security, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
          Text("Zero-Knowledge Cryptography", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        Text("• 100% Client-Side AES-256 GCM Media Encryption\n• Isolated Android StrongBox KeyStore integration\n• Zero third-party ad telemetry or analytics tracking", fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)
      }
    }

    // Toggle: Private Search
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text("Incognito Search & Discovery", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Do not suggest my profile in global search recommendations", fontSize = 11.sp, color = TextSecondary)
      }
      Switch(
        checked = isPrivateSearch,
        onCheckedChange = { isPrivateSearch = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = CyanAccent)
      )
    }

    // Toggle: Read Receipts
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text("Read Receipts in Messenger", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Send 'Seen' checkmarks when opening encrypted chats", fontSize = 11.sp, color = TextSecondary)
      }
      Switch(
        checked = isReadReceipts,
        onCheckedChange = { isReadReceipts = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = EmeraldPrimary)
      )
    }

    HorizontalDivider(color = ObsidianBorder)

    // Data Archive Download
    Button(
      onClick = { downloadRequested = true },
      colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceVariant),
      border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Icon(Icons.Default.Download, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = if (downloadRequested) "Archive Generation Queued (JSON)" else "Download My Account Data Archive",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )
    }
  }
}

// =========================================================================
// Reusable In-Tree Modal Wrapper (Completely prevents SurfaceSyncGroup Window timeout)
// =========================================================================
@Composable
private fun ModalContainer(
  title: String,
  subtitle: String,
  icon: ImageVector,
  iconTint: Color,
  onDismiss: () -> Unit,
  content: @Composable () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black.copy(alpha = 0.75f))
      .clickable(enabled = true, onClick = onDismiss)
      .padding(16.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      shape = RoundedCornerShape(20.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
      modifier = Modifier
        .fillMaxWidth()
        .clickable(enabled = true, onClick = { /* consume click */ })
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState()),
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
                .size(38.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
              Text(subtitle, fontSize = 11.sp, color = TextSecondary)
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

        content()
      }
    }
  }
}
