package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Reel
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReelsScreen(
  reels: List<Reel>,
  activeReelIndex: Int,
  isCommentsOpen: Boolean,
  onSetActiveReel: (Int) -> Unit,
  onToggleLike: (String) -> Unit,
  onOpenComments: () -> Unit,
  onCloseComments: () -> Unit,
  onAddComment: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val pagerState = rememberPagerState(initialPage = activeReelIndex, pageCount = { reels.size })

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    VerticalPager(
      state = pagerState,
      modifier = Modifier.fillMaxSize()
    ) { page ->
      val reel = reels[page]
      ReelItem(
        reel = reel,
        onToggleLike = { onToggleLike(reel.id) },
        onOpenComments = onOpenComments
      )
    }

    // Comment Sheet
    if (isCommentsOpen) {
      val currentReel = reels.getOrNull(pagerState.currentPage) ?: reels.first()
      ReelCommentSheet(
        reel = currentReel,
        onDismiss = onCloseComments,
        onAddComment = { text -> onAddComment(currentReel.id, text) }
      )
    }
  }
}

@Composable
private fun ReelItem(
  reel: Reel,
  onToggleLike: () -> Unit,
  onOpenComments: () -> Unit
) {
  var isMuted by remember { mutableStateOf(false) }

  // Rotating vinyl record animation
  val rotationTransition = rememberInfiniteTransition(label = "vinylSpin")
  val rotationAngle by rotationTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotationAngle"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    // Media content
    AsyncImage(
      model = reel.videoThumbnail,
      contentDescription = reel.description,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // Dark Gradient overlay for text readability
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Black.copy(alpha = 0.3f),
              Color.Transparent,
              Color.Black.copy(alpha = 0.85f)
            )
          )
        )
    )

    // Sound toggle at top right
    IconButton(
      onClick = { isMuted = !isMuted },
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(16.dp)
        .size(40.dp)
        .clip(CircleShape)
        .background(Color.Black.copy(alpha = 0.5f))
    ) {
      Icon(
        imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
        contentDescription = "Toggle Mute",
        tint = Color.White,
        modifier = Modifier.size(20.dp)
      )
    }

    // Right Action Rail (Likes, Comments, Share, Audio Disc)
    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 12.dp, bottom = 80.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      // Creator Avatar with Follow Badge
      Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
      ) {
        AsyncImage(
          model = reel.creatorAvatar,
          contentDescription = reel.creatorName,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .border(2.dp, EmeraldPrimary, CircleShape)
        )

        Box(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .size(18.dp)
            .clip(CircleShape)
            .background(EmeraldPrimary),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Follow",
            tint = Color.Black,
            modifier = Modifier.size(12.dp)
          )
        }
      }

      // Like Action
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .clickable(onClick = onToggleLike)
          .testTag("reel_like_${reel.id}")
      ) {
        Icon(
          imageVector = if (reel.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = "Like",
          tint = if (reel.isLiked) RoseDanger else Color.White,
          modifier = Modifier.size(32.dp)
        )
        Text(
          text = reel.likesCount.toString(),
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }

      // Comment Action
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .clickable(onClick = onOpenComments)
          .testTag("reel_comment_${reel.id}")
      ) {
        Icon(
          imageVector = Icons.Default.ChatBubbleOutline,
          contentDescription = "Comments",
          tint = Color.White,
          modifier = Modifier.size(30.dp)
        )
        Text(
          text = reel.commentsCount.toString(),
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }

      // Share Action
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = Icons.Default.Share,
          contentDescription = "Share",
          tint = Color.White,
          modifier = Modifier.size(28.dp)
        )
        Text(
          text = reel.sharesCount.toString(),
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }

      // Spinning Vinyl Record
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color(0xFF1E222D))
          .border(1.5.dp, CyanAccent, CircleShape)
          .rotate(rotationAngle),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.MusicNote,
          contentDescription = "Audio Disc",
          tint = CyanAccent,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // Bottom Metadata & Track
    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .fillMaxWidth(0.82f)
        .padding(start = 16.dp, bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = reel.creatorHandle,
          color = Color.White,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 15.sp
        )
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(EmeraldPrimary)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text("PRO CREATOR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
      }

      Text(
        text = reel.description,
        color = Color.White,
        fontSize = 13.sp,
        maxLines = 2,
        lineHeight = 18.sp
      )

      // Audio Track Marquee
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(Color.Black.copy(alpha = 0.5f))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.GraphicEq,
          contentDescription = "Audio track",
          tint = CyanAccent,
          modifier = Modifier.size(14.dp)
        )
        Text(
          text = reel.audioTrack,
          color = CyanAccent,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          maxLines = 1
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReelCommentSheet(
  reel: Reel,
  onDismiss: () -> Unit,
  onAddComment: (String) -> Unit
) {
  var commentInput by remember { mutableStateOf("") }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = ObsidianSurface,
    contentColor = TextPrimary
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Text(
        text = "Comments (${reel.comments.size})",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = TextPrimary,
        modifier = Modifier.padding(bottom = 12.dp)
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        reel.comments.forEach { comment ->
          Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
          ) {
            AsyncImage(
              model = comment.authorAvatar,
              contentDescription = comment.authorName,
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
            )
            Column {
              Text(
                text = comment.authorName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = comment.text,
                fontSize = 13.sp,
                color = TextSecondary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Input
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = commentInput,
          onValueChange = { commentInput = it },
          placeholder = { Text("Add comment...", fontSize = 13.sp, color = TextMuted) },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(24.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EmeraldPrimary,
            unfocusedBorderColor = ObsidianBorder
          )
        )

        IconButton(
          onClick = {
            if (commentInput.isNotBlank()) {
              onAddComment(commentInput)
              commentInput = ""
            }
          },
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(EmeraldPrimary)
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = Color.Black
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
