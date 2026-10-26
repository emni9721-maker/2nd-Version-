package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.SocialPost
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun PostsScreen(
  posts: List<SocialPost>,
  onToggleLike: (String) -> Unit,
  onAddComment: (String, String) -> Unit,
  onOpenUpload: (() -> Unit)? = null,
  onBackToFeed: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  BackHandler(enabled = onBackToFeed != null) {
    onBackToFeed?.invoke()
  }

  var selectedTag by remember { mutableStateOf("All") }
  val tags = listOf("All", "#Cyberpunk", "#NeoTokyo", "#Encrypted", "#Photography", "#Security", "#AI")

  val filteredPosts = remember(posts, selectedTag) {
    if (selectedTag == "All") posts
    else posts.filter { post -> post.tags.any { it.equals(selectedTag, ignoreCase = true) } }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("posts_screen_list"),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header & Tags Filter
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, top = 16.dp)
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
            if (onBackToFeed != null) {
              IconButton(
                onClick = onBackToFeed,
                modifier = Modifier
                  .size(36.dp)
                  .testTag("posts_back_to_feed")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back to Feed",
                  tint = TextPrimary
                )
              }
            }
            Column {
              Text(
                text = "Community Posts",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
              )
              Text(
                text = "Shared photos, media & encrypted cloud moments",
                fontSize = 12.sp,
                color = TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(vertical = 4.dp)
        ) {
          items(tags) { tag ->
            val isSelected = tag == selectedTag
            FilterChip(
              selected = isSelected,
              onClick = { selectedTag = tag },
              label = {
                Text(
                  text = tag,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.Black else TextPrimary
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = EmeraldPrimary,
                containerColor = MaterialTheme.colorScheme.surface
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = ObsidianBorder,
                selectedBorderColor = EmeraldPrimary,
                enabled = true,
                selected = isSelected
              )
            )
          }
        }
      }
    }

    // List of Posts
    items(filteredPosts, key = { it.id }) { post ->
      CommunityPostCard(
        post = post,
        onToggleLike = { onToggleLike(post.id) },
        onAddComment = { text -> onAddComment(post.id, text) }
      )
    }

    if (filteredPosts.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No posts found for $selectedTag",
            color = TextMuted,
            fontSize = 14.sp
          )
        }
      }
    }
  }
}

@Composable
private fun CommunityPostCard(
  post: SocialPost,
  onToggleLike: () -> Unit,
  onAddComment: (String) -> Unit
) {
  var isCommentExpanded by remember { mutableStateOf(false) }
  var commentInput by remember { mutableStateOf("") }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .testTag("post_card_${post.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
  ) {
    Column {
      // Author Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        AsyncImage(
          model = post.authorAvatar,
          contentDescription = post.authorName,
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.5.dp, EmeraldPrimary, CircleShape),
          contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = post.authorName,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Verified",
              tint = CyanAccent,
              modifier = Modifier.size(13.dp)
            )
          }
          Text(
            text = "${post.authorHandle} • ${post.timestamp}",
            fontSize = 11.sp,
            color = TextSecondary
          )
        }
      }

      // Media Image
      AsyncImage(
        model = post.mediaUrl,
        contentDescription = post.caption,
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1.25f)
          .background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = ContentScale.Crop
      )

      // Post Content & Actions
      Column(modifier = Modifier.padding(14.dp)) {
        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            // Like Button
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clickable(onClick = onToggleLike)
                .testTag("post_like_${post.id}")
            ) {
              Icon(
                imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Like",
                tint = if (post.isLiked) RoseDanger else TextSecondary,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "${post.likesCount}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (post.isLiked) RoseDanger else TextSecondary
              )
            }

            // Comment Button
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clickable { isCommentExpanded = !isCommentExpanded }
                .testTag("post_comment_btn_${post.id}")
            ) {
              Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = "Comment",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "${post.comments.size}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
              )
            }
          }

          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            tint = TextSecondary,
            modifier = Modifier.size(19.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Caption
        Text(
          text = post.caption,
          fontSize = 13.sp,
          color = TextPrimary,
          lineHeight = 18.sp
        )

        // Tags
        if (post.tags.isNotEmpty()) {
          Spacer(modifier = Modifier.height(6.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            post.tags.forEach { tag ->
              Text(
                text = tag,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EmeraldPrimary
              )
            }
          }
        }

        // Expandable Comments Section
        AnimatedVisibility(visible = isCommentExpanded) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 12.dp)
          ) {
            post.comments.takeLast(3).forEach { comment ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = comment.authorName,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = comment.text,
                  fontSize = 12.sp,
                  color = TextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = commentInput,
                onValueChange = { commentInput = it },
                placeholder = { Text("Write a comment...", fontSize = 12.sp, color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                  .weight(1f)
                  .testTag("comment_input_${post.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = EmeraldPrimary,
                  unfocusedBorderColor = ObsidianBorder,
                  focusedContainerColor = MaterialTheme.colorScheme.background,
                  unfocusedContainerColor = MaterialTheme.colorScheme.background,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary
                )
              )

              Spacer(modifier = Modifier.width(8.dp))

              IconButton(
                onClick = {
                  if (commentInput.isNotBlank()) {
                    onAddComment(commentInput.trim())
                    commentInput = ""
                  }
                },
                enabled = commentInput.isNotBlank(),
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(if (commentInput.isNotBlank()) EmeraldPrimary else ObsidianSurfaceVariant)
                  .testTag("comment_send_${post.id}")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.Send,
                  contentDescription = "Send",
                  tint = if (commentInput.isNotBlank()) Color.Black else TextMuted,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
