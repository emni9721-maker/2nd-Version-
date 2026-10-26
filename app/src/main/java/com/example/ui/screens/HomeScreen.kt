package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.SocialPost
import com.example.data.Story
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
  posts: List<SocialPost>,
  stories: List<Story>,
  onOpenStory: (Story) -> Unit,
  onToggleLike: (String) -> Unit,
  onAddComment: (String, String) -> Unit,
  onOpenUpload: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // 24-Hour Stories Tray
    item {
      StoriesTray(
        stories = stories,
        onOpenStory = onOpenStory,
        onAddStory = onOpenUpload
      )
    }

    // Posts Feed
    items(posts, key = { it.id }) { post ->
      SocialPostCard(
        post = post,
        onToggleLike = { onToggleLike(post.id) },
        onAddComment = { text -> onAddComment(post.id, text) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
private fun StoriesTray(
  stories: List<Story>,
  onOpenStory: (Story) -> Unit,
  onAddStory: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .border(1.dp, ObsidianBorder.copy(alpha = 0.3f))
      .padding(vertical = 12.dp)
  ) {
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(14.dp),
      modifier = Modifier.padding(horizontal = 16.dp)
    ) {
      // User's own story addition
      item {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable(onClick = onAddStory)
            .testTag("add_story_button")
        ) {
          Box(
            modifier = Modifier
              .size(62.dp)
              .clip(CircleShape)
              .background(ObsidianSurfaceVariant)
              .border(1.5.dp, ObsidianBorder, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Add Story",
              tint = EmeraldPrimary,
              modifier = Modifier.size(28.dp)
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Your Story",
            fontSize = 11.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Stories from other users
      items(stories, key = { it.id }) { story ->
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onOpenStory(story) }
            .testTag("story_item_${story.id}")
        ) {
          val ringBrush = if (story.hasUnseen) {
            Brush.linearGradient(listOf(EmeraldPrimary, CyanAccent))
          } else {
            Brush.linearGradient(listOf(ObsidianBorder, ObsidianBorder))
          }

          Box(
            modifier = Modifier
              .size(62.dp)
              .clip(CircleShape)
              .border(2.dp, ringBrush, CircleShape)
              .padding(3.dp),
            contentAlignment = Alignment.Center
          ) {
            AsyncImage(
              model = story.authorAvatar,
              contentDescription = story.authorName,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = story.authorName.split(" ").first(),
            fontSize = 11.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

@Composable
private fun SocialPostCard(
  post: SocialPost,
  onToggleLike: () -> Unit,
  onAddComment: (String) -> Unit
) {
  var isCommentsExpanded by remember { mutableStateOf(false) }
  var commentInput by remember { mutableStateOf("") }

  val heartScale by animateFloatAsState(
    targetValue = if (post.isLiked) 1.25f else 1.0f,
    animationSpec = spring(dampingRatio = 0.4f),
    label = "heartScale"
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 8.dp)
      .testTag("post_card_${post.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          AsyncImage(
            model = post.authorAvatar,
            contentDescription = post.authorName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .border(1.dp, EmeraldPrimary.copy(alpha = 0.5f), CircleShape)
          )
          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = post.authorName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextPrimary
              )
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verified",
                tint = CyanAccent,
                modifier = Modifier.size(14.dp)
              )
            }
            Text(
              text = "${post.authorHandle} • ${post.timestamp}",
              fontSize = 11.sp,
              color = TextMuted
            )
          }
        }
      }

      // Caption
      Text(
        text = post.caption,
        fontSize = 14.sp,
        color = TextPrimary,
        lineHeight = 20.sp,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
      )

      if (post.tags.isNotEmpty()) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          post.tags.forEach { tag ->
            Text(
              text = tag,
              color = CyanAccent,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // Media Image / Video Container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
          .padding(top = 8.dp)
          .background(ObsidianSurfaceVariant)
      ) {
        AsyncImage(
          model = post.mediaUrl,
          contentDescription = "Post Media",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        if (post.isVideo) {
          Box(
            modifier = Modifier
              .align(Alignment.Center)
              .size(54.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.65f))
              .border(1.5.dp, EmeraldPrimary, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = "Play Video",
              tint = EmeraldPrimary,
              modifier = Modifier.size(32.dp)
            )
          }
        }
      }

      // Action Bar: Likes, Comments, Share
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Like Button
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable(onClick = onToggleLike)
              .padding(4.dp)
              .testTag("like_button_${post.id}")
          ) {
            Icon(
              imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (post.isLiked) RoseDanger else TextSecondary,
              modifier = Modifier
                .size(22.dp)
                .scale(heartScale)
            )
            Text(
              text = post.likesCount.toString(),
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = if (post.isLiked) RoseDanger else TextSecondary
            )
          }

          // Comment Button
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { isCommentsExpanded = !isCommentsExpanded }
              .padding(4.dp)
              .testTag("comment_button_${post.id}")
          ) {
            Icon(
              imageVector = Icons.Default.ChatBubbleOutline,
              contentDescription = "Comments",
              tint = TextSecondary,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = post.commentsCount.toString(),
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextSecondary
            )
          }
        }

        // Share Button
        IconButton(onClick = {}) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Expandable Comments Section
      AnimatedVisibility(visible = isCommentsExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(ObsidianSurfaceVariant.copy(alpha = 0.5f))
            .padding(14.dp)
        ) {
          Text(
            text = "Comments (${post.comments.size})",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
          )

          post.comments.forEach { comment ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              AsyncImage(
                model = comment.authorAvatar,
                contentDescription = comment.authorName,
                modifier = Modifier
                  .size(26.dp)
                  .clip(CircleShape)
              )
              Column {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Text(
                    text = comment.authorName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                  Text(
                    text = comment.timestamp,
                    fontSize = 10.sp,
                    color = TextMuted
                  )
                }
                Text(
                  text = comment.text,
                  fontSize = 12.sp,
                  color = TextSecondary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Comment Input
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = commentInput,
              onValueChange = { commentInput = it },
              placeholder = { Text("Write an encrypted comment...", fontSize = 12.sp, color = TextMuted) },
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("comment_input_${post.id}"),
              shape = RoundedCornerShape(24.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = ObsidianBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              ),
              singleLine = true
            )

            IconButton(
              onClick = {
                if (commentInput.isNotBlank()) {
                  onAddComment(commentInput)
                  commentInput = ""
                }
              },
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary)
                .testTag("send_comment_${post.id}")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }
  }
}
