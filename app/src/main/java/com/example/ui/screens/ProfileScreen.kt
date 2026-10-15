package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import coil.compose.AsyncImage
import com.example.data.SocialPost
import com.example.data.VaultItem
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

import com.example.data.UserProfile
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
  posts: List<SocialPost>,
  vaultItems: List<VaultItem>,
  currentUser: FirebaseUser? = null,
  userProfile: UserProfile? = null,
  onOpenSettings: () -> Unit,
  onSignOut: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Posts", "Public Albums", "Hardware Keys")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Header & Cover Banner
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
          .background(
            Brush.linearGradient(
              listOf(Color(0xFF0F1E1B), Color(0xFF0B1726), Color(0xFF1B142E))
            )
          )
      ) {
        Row(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(Color.Black.copy(alpha = 0.5f))
              .border(1.dp, CyanAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CloudDone,
                contentDescription = "Synced",
                tint = CyanAccent,
                modifier = Modifier.size(12.dp)
              )
              Text("StrongBox 256", fontSize = 10.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Avatar & User Info
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          // Avatar overlapping cover
          AsyncImage(
            model = currentUser?.photoUrl?.toString() ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
            contentDescription = "Profile Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .border(3.dp, EmeraldPrimary, CircleShape)
          )

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = onOpenSettings,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
              border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
              shape = RoundedCornerShape(20.dp),
              modifier = Modifier.testTag("manage_cloud_button")
            ) {
              Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Security Settings",
                tint = EmeraldPrimary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Cloud Tier", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
              onClick = onSignOut,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF43F5E)),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF43F5E).copy(alpha = 0.5f)),
              shape = RoundedCornerShape(20.dp),
              modifier = Modifier.testTag("sign_out_button")
            ) {
              Text("Sign Out", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = currentUser?.displayName ?: "Elena Vance",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
          )
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Verified Master",
            tint = CyanAccent,
            modifier = Modifier.size(16.dp)
          )
        }

        Text(
          text = currentUser?.email ?: "@elena_sec • Zero Architect",
          fontSize = 13.sp,
          color = TextSecondary
        )

        Text(
          text = userProfile?.bio ?: "Zero-knowledge private cloud curator. 100% hardware-encrypted photography & 4K cinematic reels.",
          fontSize = 13.sp,
          color = TextPrimary,
          lineHeight = 18.sp,
          modifier = Modifier.padding(vertical = 8.dp)
        )

        // Stats Counters
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          StatItem(value = vaultItems.size.toString(), label = "Vault Items")
          StatItem(value = "${(userProfile?.followersCount ?: 1420L) / 1000.0}K", label = "Followers")
          StatItem(value = (userProfile?.followingCount ?: 342L).toString(), label = "Following")
          StatItem(value = "85%", label = "Avg. Compressed")
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quota Progress Card
        Card(
          colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceVariant),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Cloud Vault Storage",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "18.4 GB of 50 GB Used (36%)",
                fontSize = 11.sp,
                color = CyanAccent,
                fontWeight = FontWeight.SemiBold
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
              progress = { 0.368f },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = EmeraldPrimary,
              trackColor = ObsidianBorder
            )
          }
        }
      }
    }

    // Tab Navigation
    item {
      Spacer(modifier = Modifier.height(14.dp))
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = EmeraldPrimary
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
              )
            }
          )
        }
      }
    }

    // Tab Contents
    item {
      when (selectedTab) {
        0 -> {
          // Posts Grid
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            posts.forEach { post ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(MaterialTheme.colorScheme.surface)
                  .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                AsyncImage(
                  model = post.mediaUrl,
                  contentDescription = post.caption,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                )
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = post.caption,
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                  )
                  Text(
                    text = "${post.likesCount} likes • ${post.commentsCount} comments",
                    fontSize = 10.sp,
                    color = TextSecondary
                  )
                }
              }
            }
          }
        }
        1 -> {
          // Public Albums
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            AlbumCard(title = "Cinematic Nightscapes", count = "14 photos", coverUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=400&q=80")
            AlbumCard(title = "Obsidian Architecture", count = "8 photos", coverUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=400&q=80")
          }
        }
        else -> {
          // Hardware Keys
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            KeyBadge(name = "Master AES-256 StrongBox Key", alias = "zero_master_sec01", status = "Hardware Isolated")
            KeyBadge(name = "ChaCha20 Stream Key", alias = "zero_stream_video_81", status = "Active")
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
private fun StatItem(value: String, label: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      fontSize = 15.sp,
      fontWeight = FontWeight.ExtraBold,
      color = TextPrimary
    )
    Text(
      text = label,
      fontSize = 11.sp,
      color = TextMuted
    )
  }
}

@Composable
private fun AlbumCard(title: String, count: String, coverUrl: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surface)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
      .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    AsyncImage(
      model = coverUrl,
      contentDescription = title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(60.dp)
        .clip(RoundedCornerShape(8.dp))
    )
    Column {
      Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
      Text(text = count, fontSize = 11.sp, color = TextSecondary)
    }
  }
}

@Composable
private fun KeyBadge(name: String, alias: String, status: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surface)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Icon(
        imageVector = Icons.Default.VpnKey,
        contentDescription = "Key",
        tint = EmeraldPrimary,
        modifier = Modifier.size(20.dp)
      )
      Column {
        Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(text = "Alias: $alias", fontSize = 11.sp, color = TextMuted)
      }
    }

    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(EmeraldDark.copy(alpha = 0.3f))
        .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Text(text = status, fontSize = 10.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
    }
  }
}
