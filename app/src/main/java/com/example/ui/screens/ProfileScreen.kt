package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Reel
import com.example.data.SocialPost
import com.example.data.UserProfile
import com.example.data.VaultItem
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
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
  posts: List<SocialPost>,
  vaultItems: List<VaultItem>,
  reels: List<Reel> = emptyList(),
  currentUser: FirebaseUser? = null,
  userProfile: UserProfile? = null,
  onOpenEditProfile: () -> Unit = {},
  onOpenReel: ((Int) -> Unit)? = null,
  onOpenUpload: (() -> Unit)? = null,
  onOpenSettings: (() -> Unit)? = null,
  onSignOut: (() -> Unit)? = null,
  onBackToFeed: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  BackHandler(enabled = onBackToFeed != null) {
    onBackToFeed?.invoke()
  }

  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Posts", "Reels", "Public Albums")
  var showOptionsMenu by remember { mutableStateOf(false) }

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
        if (onBackToFeed != null) {
          IconButton(
            onClick = onBackToFeed,
            modifier = Modifier
              .align(Alignment.TopStart)
              .padding(12.dp)
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.5f))
              .testTag("profile_back_to_feed")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Feed",
              tint = TextPrimary,
              modifier = Modifier.size(20.dp)
            )
          }
        }

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

    // Avatar & User Info & 3-Dot Options
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
            model = userProfile?.photoUrl?.ifBlank { currentUser?.photoUrl?.toString() }
              ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
            contentDescription = "Profile Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .border(3.dp, EmeraldPrimary, CircleShape)
          )

          // Right row: End-to-End Encrypted badge + 3-Dot Options button (...)
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(ObsidianSurfaceVariant)
                .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary)
                )
                Text(
                  text = "End-to-End Encrypted",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = EmeraldPrimary
                )
              }
            }

            // 3-Dot Options button (...) for Profile Editing
            Box {
              IconButton(
                onClick = { showOptionsMenu = true },
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(ObsidianSurfaceVariant)
                  .border(1.dp, ObsidianBorder, CircleShape)
                  .testTag("profile_options_dots_button")
              ) {
                Icon(
                  imageVector = Icons.Default.MoreHoriz,
                  contentDescription = "Profile Options",
                  tint = TextPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }

              DropdownMenu(
                expanded = showOptionsMenu,
                onDismissRequest = { showOptionsMenu = false },
                modifier = Modifier
                  .background(ObsidianSurfaceElevated)
                  .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
              ) {
                DropdownMenuItem(
                  text = {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(18.dp)
                      )
                      Column {
                        Text(
                          text = "Edit Profile / প্রোফাইল এডিট",
                          fontSize = 13.sp,
                          fontWeight = FontWeight.Bold,
                          color = TextPrimary
                        )
                        Text(
                          text = "Name, DOB, Location, School",
                          fontSize = 10.sp,
                          color = TextSecondary
                        )
                      }
                    }
                  },
                  onClick = {
                    showOptionsMenu = false
                    onOpenEditProfile()
                  },
                  modifier = Modifier.testTag("menu_edit_profile_action")
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Name & Verification Badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = userProfile?.displayName?.ifBlank { currentUser?.displayName } ?: "Elena Vance",
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
          text = userProfile?.username?.ifBlank { currentUser?.email } ?: "@elena_sec • Zero Architect",
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

        // Facebook-style Intro / Public Details Card
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("facebook_style_intro_card")
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Details",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "Edit",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary,
                modifier = Modifier
                  .clickable { onOpenEditProfile() }
                  .testTag("edit_details_shortcut")
              )
            }

            HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

            // School / College
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.School, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
              Text(
                text = "Studied at ${userProfile?.schoolCollege ?: "Dhaka College / University of Dhaka"}",
                fontSize = 12.sp,
                color = TextPrimary
              )
            }

            // Location
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
              Text(
                text = "Lives in ${userProfile?.location ?: "Dhaka, Bangladesh"}",
                fontSize = 12.sp,
                color = TextPrimary
              )
            }

            // Date of Birth
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.Cake, contentDescription = null, tint = RoseDanger, modifier = Modifier.size(16.dp))
              Text(
                text = "Born on ${userProfile?.dateOfBirth ?: "15 August 2000"}",
                fontSize = 12.sp,
                color = TextPrimary
              )
            }

            // Workplace
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.Business, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
              Text(
                text = "Works at ${userProfile?.workplace ?: "Zero Cloud Security Lab"}",
                fontSize = 12.sp,
                color = TextPrimary
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Edit public details button (Facebook style)
            OutlinedButton(
              onClick = onOpenEditProfile,
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
              border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("edit_public_details_button")
            ) {
              Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Edit public details",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

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
          StatItem(value = posts.size.toString(), label = "Posts")
          StatItem(value = reels.size.toString(), label = "Reels")
          StatItem(value = "${(userProfile?.followersCount ?: 1420L) / 1000.0}K", label = "Followers")
          StatItem(value = (userProfile?.followingCount ?: 342L).toString(), label = "Following")
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
                text = "Cloud Storage",
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

    // Tab Navigation: Posts | Reels | Public Albums
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
            },
            modifier = Modifier.testTag("profile_tab_${title.lowercase()}")
          )
        }
      }
    }

    // Tab Contents
    item {
      when (selectedTab) {
        0 -> {
          // Tab 0: Posts Grid
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
          // Tab 1: REELS Tab (Replacing Hardware Keys)
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Uploaded Reels (${reels.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              if (onOpenUpload != null) {
                Button(
                  onClick = onOpenUpload,
                  colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.testTag("upload_reel_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("New Reel", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
              }
            }

            if (reels.isEmpty()) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(14.dp))
                  .background(MaterialTheme.colorScheme.surface)
                  .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
                  .padding(32.dp),
                contentAlignment = Alignment.Center
              ) {
                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = null,
                    tint = CyanAccent,
                    modifier = Modifier.size(36.dp)
                  )
                  Text("No reels uploaded yet", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                  Text("Upload your first 4K encrypted reel", fontSize = 11.sp, color = TextSecondary)
                }
              }
            } else {
              // 3-Column Reels Grid (Instagram / Facebook style)
              val chunkedReels = reels.chunked(3)
              chunkedReels.forEach { rowReels ->
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  rowReels.forEachIndexed { indexInRow, reel ->
                    val reelIndex = reels.indexOf(reel)
                    Box(
                      modifier = Modifier
                        .weight(1f)
                        .aspectRatio(9f / 14f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ObsidianSurfaceVariant)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(8.dp))
                        .clickable { onOpenReel?.invoke(reelIndex) }
                        .testTag("profile_reel_card_$reelIndex")
                    ) {
                      AsyncImage(
                        model = reel.videoThumbnail,
                        contentDescription = reel.description,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                      )

                      // Gradient overlay
                      Box(
                        modifier = Modifier
                          .fillMaxSize()
                          .background(
                            Brush.verticalGradient(
                              colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                              ),
                              startY = 100f
                            )
                          )
                      )

                      // Play icon badge top right
                      Box(
                        modifier = Modifier
                          .align(Alignment.TopEnd)
                          .padding(6.dp)
                          .size(22.dp)
                          .clip(CircleShape)
                          .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                      ) {
                        Icon(
                          imageVector = Icons.Default.PlayArrow,
                          contentDescription = "Play",
                          tint = Color.White,
                          modifier = Modifier.size(14.dp)
                        )
                      }

                      // Caption & Likes at bottom
                      Column(
                        modifier = Modifier
                          .align(Alignment.BottomStart)
                          .padding(6.dp)
                      ) {
                        Row(
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                          Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Likes",
                            tint = RoseDanger,
                            modifier = Modifier.size(10.dp)
                          )
                          Text(
                            text = reel.likesCount.toString(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                          )
                        }
                        Text(
                          text = reel.description,
                          fontSize = 9.sp,
                          color = Color.White.copy(alpha = 0.9f),
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                        )
                      }
                    }
                  }

                  // Fill remaining space if row has less than 3 items
                  repeat(3 - rowReels.size) {
                    Spacer(modifier = Modifier.weight(1f))
                  }
                }
              }
            }
          }
        }
        else -> {
          // Tab 2: Public Albums
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
