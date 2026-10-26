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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ChatMessage
import com.example.data.Conversation
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerSheet(
  conversations: List<Conversation>,
  activeConversation: Conversation?,
  onSelectConversation: (Conversation) -> Unit,
  onSendMessage: (String, String) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var messageInput by remember { mutableStateOf("") }
  var currentConv by remember(activeConversation) { mutableStateOf(activeConversation) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = ObsidianSurface,
    contentColor = TextPrimary
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .height(580.dp)
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      if (currentConv == null) {
        // Conversations List View
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
              imageVector = Icons.Default.Lock,
              contentDescription = "Encrypted Chat",
              tint = EmeraldPrimary,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "Encrypted Direct Messenger",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          items(conversations, key = { it.id }) { conv ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurfaceVariant)
                .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
                .clickable {
                  currentConv = conv
                  onSelectConversation(conv)
                }
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Box {
                  AsyncImage(
                    model = conv.participantAvatar,
                    contentDescription = conv.participantName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                      .size(44.dp)
                      .clip(CircleShape)
                  )
                  if (conv.isOnline) {
                    Box(
                      modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary)
                        .border(2.dp, ObsidianSurfaceVariant, CircleShape)
                    )
                  }
                }

                Column {
                  Text(
                    text = conv.participantName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                  Text(
                    text = conv.lastMessage,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1
                  )
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = conv.lastMessageTime,
                  fontSize = 10.sp,
                  color = TextMuted
                )
                if (conv.unreadCount > 0) {
                  Box(
                    modifier = Modifier
                      .padding(top = 4.dp)
                      .clip(CircleShape)
                      .background(EmeraldPrimary)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = conv.unreadCount.toString(),
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.Black
                    )
                  }
                }
              }
            }
          }
        }
      } else {
        // Active Chat Thread View
        val conv = currentConv!!

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            IconButton(onClick = { currentConv = null }) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary
              )
            }

            AsyncImage(
              model = conv.participantAvatar,
              contentDescription = conv.participantName,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
            )

            Column {
              Text(
                text = conv.participantName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "End-to-End Encrypted (Zero-Knowledge)",
                fontSize = 10.sp,
                color = CyanAccent
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Message bubbles
        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(conv.messages, key = { it.id }) { msg ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = if (msg.isMine) Arrangement.End else Arrangement.Start
            ) {
              Box(
                modifier = Modifier
                  .clip(
                    RoundedCornerShape(
                      topStart = 14.dp,
                      topEnd = 14.dp,
                      bottomStart = if (msg.isMine) 14.dp else 2.dp,
                      bottomEnd = if (msg.isMine) 2.dp else 14.dp
                    )
                  )
                  .background(if (msg.isMine) EmeraldPrimary else ObsidianSurfaceVariant)
                  .padding(horizontal = 12.dp, vertical = 8.dp)
              ) {
                Column {
                  Text(
                    text = msg.text,
                    fontSize = 13.sp,
                    color = if (msg.isMine) Color.Black else TextPrimary
                  )
                  Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Text(
                      text = msg.timestamp,
                      fontSize = 9.sp,
                      color = if (msg.isMine) Color.Black.copy(alpha = 0.6f) else TextMuted
                    )
                    Icon(
                      imageVector = Icons.Default.Lock,
                      contentDescription = "Encrypted",
                      tint = if (msg.isMine) Color.Black.copy(alpha = 0.6f) else CyanAccent,
                      modifier = Modifier.size(10.dp)
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Input
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = messageInput,
            onValueChange = { messageInput = it },
            placeholder = { Text("Encrypted message...", fontSize = 12.sp, color = TextMuted) },
            modifier = Modifier
              .weight(1f)
              .testTag("chat_input_field"),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              unfocusedBorderColor = ObsidianBorder
            )
          )

          IconButton(
            onClick = {
              if (messageInput.isNotBlank()) {
                onSendMessage(conv.id, messageInput)
                messageInput = ""
              }
            },
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary)
              .testTag("chat_send_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send",
              tint = Color.Black
            )
          }
        }
      }
    }
  }
}
