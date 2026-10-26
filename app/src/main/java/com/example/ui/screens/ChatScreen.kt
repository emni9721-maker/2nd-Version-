package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ChatMessage
import com.example.data.Conversation
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
  conversations: List<Conversation>,
  activeConversation: Conversation?,
  onSelectConversation: (Conversation) -> Unit,
  onSendMessage: (String, String) -> Unit,
  onSendFileMessage: (conversationId: String, fileName: String, fileSizeFormatted: String, fileExtension: String) -> Unit,
  onBackToFeed: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  var selectedConv by remember(activeConversation) { mutableStateOf(activeConversation) }
  var messageInput by remember { mutableStateOf("") }
  var showAttachmentSheet by remember { mutableStateOf(false) }
  var searchQuery by remember { mutableStateOf("") }

  val whatsAppReadCheck = Color(0xFF53BDEB)

  // Handle system back navigation
  BackHandler(enabled = true) {
    if (selectedConv != null) {
      selectedConv = null
    } else {
      onBackToFeed?.invoke()
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    if (selectedConv == null) {
      // -------------------------------------------------------------
      // 1. WhatsApp-Style Conversations List View
      // -------------------------------------------------------------
      Column(
        modifier = Modifier
          .fillMaxSize()
          .testTag("chat_conversations_list_screen")
      ) {
        // WhatsApp Top App Bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F2C34))
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
                  modifier = Modifier.testTag("chat_back_to_feed")
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Feed",
                    tint = TextPrimary
                  )
                }
              }

              Text(
                text = "WhatsApp Chat",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(EmeraldPrimary.copy(alpha = 0.2f))
                  .border(1.dp, EmeraldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "E2EE",
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(12.dp)
                  )
                  Text("Original File Sender", fontSize = 10.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }

        // WhatsApp Search bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search chats or messages...", fontSize = 13.sp, color = TextMuted) },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TextMuted, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color(0xFF1F2C34),
              unfocusedContainerColor = Color(0xFF1F2C34),
              focusedBorderColor = EmeraldPrimary,
              unfocusedBorderColor = Color.Transparent
            )
          )
        }

        // Chats List
        val filteredConversations = remember(conversations, searchQuery) {
          if (searchQuery.isBlank()) conversations
          else conversations.filter {
            it.participantName.contains(searchQuery, ignoreCase = true) ||
              it.lastMessage.contains(searchQuery, ignoreCase = true)
          }
        }

        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
          contentPadding = PaddingValues(bottom = 80.dp)
        ) {
          items(filteredConversations, key = { it.id }) { conv ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  selectedConv = conv
                  onSelectConversation(conv)
                }
                .padding(horizontal = 8.dp, vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                Box {
                  AsyncImage(
                    model = conv.participantAvatar,
                    contentDescription = conv.participantName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                      .size(52.dp)
                      .clip(CircleShape)
                  )
                  if (conv.isOnline) {
                    Box(
                      modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF25D366))
                        .border(2.dp, Color(0xFF1F2C34), CircleShape)
                    )
                  }
                }

                Column(modifier = Modifier.width(220.dp)) {
                  Text(
                    text = conv.participantName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.DoneAll,
                      contentDescription = "Read",
                      tint = whatsAppReadCheck,
                      modifier = Modifier.size(14.dp)
                    )
                    Text(
                      text = conv.lastMessage,
                      fontSize = 13.sp,
                      color = TextSecondary,
                      maxLines = 1
                    )
                  }
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = conv.lastMessageTime,
                  fontSize = 11.sp,
                  color = if (conv.unreadCount > 0) Color(0xFF25D366) else TextMuted
                )
                if (conv.unreadCount > 0) {
                  Box(
                    modifier = Modifier
                      .padding(top = 4.dp)
                      .clip(CircleShape)
                      .background(Color(0xFF25D366))
                      .padding(horizontal = 7.dp, vertical = 3.dp)
                  ) {
                    Text(
                      text = conv.unreadCount.toString(),
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.Black
                    )
                  }
                }
              }
            }
          }
        }
      }
    } else {
      // -------------------------------------------------------------
      // 2. Active WhatsApp Chat Room View (with Original File Sender)
      // -------------------------------------------------------------
      val conv = selectedConv!!
      val listState = rememberLazyListState()

      LaunchedEffect(conv.messages.size) {
        if (conv.messages.isNotEmpty()) {
          listState.animateScrollToItem(conv.messages.size - 1)
        }
      }

      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(Color(0xFF0B141A)) // Classic WhatsApp Dark background
          .testTag("whatsapp_active_chat_room")
      ) {
        // WhatsApp Top Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F2C34))
            .padding(horizontal = 4.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            IconButton(
              onClick = { selectedConv = null },
              modifier = Modifier.testTag("chat_back_button")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
              )
            }

            AsyncImage(
              model = conv.participantAvatar,
              contentDescription = conv.participantName,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column {
              Text(
                text = conv.participantName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = if (conv.isOnline) "online • End-to-End Encrypted" else "last seen recently",
                fontSize = 11.sp,
                color = if (conv.isOnline) Color(0xFF25D366) else TextMuted
              )
            }
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            IconButton(onClick = {}) {
              Icon(imageVector = Icons.Default.Videocam, contentDescription = "Video Call", tint = Color.White, modifier = Modifier.size(22.dp))
            }
            IconButton(onClick = {}) {
              Icon(imageVector = Icons.Default.Call, contentDescription = "Voice Call", tint = Color.White, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = {}) {
              Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", tint = Color.White, modifier = Modifier.size(20.dp))
            }
          }
        }

        // Encryption Notice Pill
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF182229))
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(imageVector = Icons.Default.Lock, contentDescription = "Encrypted", tint = Color(0xFFFFD279), modifier = Modifier.size(12.dp))
              Text(
                text = "Messages & original files are end-to-end encrypted. No compression.",
                fontSize = 10.sp,
                color = Color(0xFFFFD279)
              )
            }
          }
        }

        // Messages List
        LazyColumn(
          state = listState,
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
          contentPadding = PaddingValues(vertical = 8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(conv.messages, key = { it.id }) { msg ->
            WhatsAppMessageBubble(msg = msg)
          }
        }

        // WhatsApp Bottom Input Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F2C34))
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .navigationBarsPadding(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Input pill
          Row(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(26.dp))
              .background(Color(0xFF2A3942))
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
              Icon(
                imageVector = Icons.Default.SentimentSatisfiedAlt,
                contentDescription = "Emoji",
                tint = TextSecondary,
                modifier = Modifier.size(22.dp)
              )
            }

            OutlinedTextField(
              value = messageInput,
              onValueChange = { messageInput = it },
              placeholder = { Text("Message", fontSize = 15.sp, color = TextMuted) },
              modifier = Modifier
                .weight(1f)
                .testTag("chat_message_input"),
              maxLines = 4,
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF25D366)
              )
            )

            // Paperclip (Attach Original Large Files)
            IconButton(
              onClick = { showAttachmentSheet = true },
              modifier = Modifier
                .size(36.dp)
                .testTag("chat_attach_button")
            ) {
              Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Attach Original Large File",
                tint = TextSecondary,
                modifier = Modifier.size(22.dp)
              )
            }

            IconButton(
              onClick = {
                // Quick photo camera
                onSendFileMessage(conv.id, "IMG_ORIGINAL_CAMERA_${System.currentTimeMillis() % 10000}.RAW", "48.2 MB", "RAW")
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Camera",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
              )
            }
          }

          // WhatsApp Round Send / Mic Button
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(Color(0xFF00A884)) // WhatsApp Green
              .clickable {
                if (messageInput.isNotBlank()) {
                  onSendMessage(conv.id, messageInput.trim())
                  messageInput = ""
                } else {
                  // If empty, tap sends a high-fidelity original sample document
                  onSendFileMessage(conv.id, "Master_Raw_Project_Archive.zip", "1.8 GB", "ZIP")
                }
              }
              .testTag("chat_send_action_button"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (messageInput.isNotBlank()) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
              contentDescription = if (messageInput.isNotBlank()) "Send" else "Voice / Quick Large File",
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }

      // WhatsApp Attachment Sheet (for sending huge uncompressed original files)
      if (showAttachmentSheet) {
        ModalBottomSheet(
          onDismissRequest = { showAttachmentSheet = false },
          containerColor = Color(0xFF1F2C34),
          contentColor = Color.White,
          sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
          AttachmentPickerContent(
            onSelectFile = { name, size, ext ->
              onSendFileMessage(conv.id, name, size, ext)
              showAttachmentSheet = false
            },
            onDismiss = { showAttachmentSheet = false }
          )
        }
      }
    }
  }
}

@Composable
private fun WhatsAppMessageBubble(msg: ChatMessage) {
  val whatsAppDarkGreen = Color(0xFF005C4B)
  val whatsAppIncomingBubble = Color(0xFF202C33)
  val whatsAppReadCheck = Color(0xFF53BDEB)

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (msg.isMine) Arrangement.End else Arrangement.Start
  ) {
    Box(
      modifier = Modifier
        .clip(
          RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
            bottomStart = if (msg.isMine) 12.dp else 2.dp,
            bottomEnd = if (msg.isMine) 2.dp else 12.dp
          )
        )
        .background(if (msg.isMine) whatsAppDarkGreen else whatsAppIncomingBubble)
        .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
      Column(modifier = Modifier.widthIn(min = 80.dp, max = 290.dp)) {
        // If message has an original large file attached
        if (!msg.fileName.isNullOrBlank()) {
          Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111B21)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 6.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(
                    when (msg.fileExtension?.uppercase()) {
                      "ZIP", "RAR", "ISO" -> Color(0xFF7C3AED)
                      "MOV", "MP4" -> Color(0xFFE11D48)
                      "RAW", "CR3" -> Color(0xFF0284C7)
                      else -> EmeraldPrimary
                    }
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (msg.fileExtension?.uppercase()) {
                    "MOV", "MP4" -> Icons.Default.Movie
                    "RAW", "CR3", "PNG" -> Icons.Default.Image
                    else -> Icons.Default.Description
                  },
                  contentDescription = msg.fileExtension ?: "File",
                  tint = Color.White,
                  modifier = Modifier.size(24.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = msg.fileName,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  maxLines = 1
                )
                Text(
                  text = "${msg.fileSizeFormatted ?: "1.4 GB"} • ${msg.fileExtension ?: "FILE"} • Original Quality",
                  fontSize = 11.sp,
                  color = CyanAccent
                )
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(imageVector = Icons.Default.Lock, contentDescription = "E2EE", tint = EmeraldPrimary, modifier = Modifier.size(10.dp))
                  Text("Zero Compression • Lossless", fontSize = 9.sp, color = EmeraldPrimary)
                }
              }

              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF2A3942)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Download,
                  contentDescription = "Download",
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }

        // Text Message
        if (msg.text.isNotBlank()) {
          Text(
            text = msg.text,
            fontSize = 14.sp,
            color = Color.White
          )
        }

        // Timestamp and double check
        Row(
          modifier = Modifier.align(Alignment.End),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = msg.timestamp,
            fontSize = 10.sp,
            color = Color(0xFF8696A0)
          )
          if (msg.isMine) {
            Icon(
              imageVector = Icons.Default.DoneAll,
              contentDescription = "Delivered & Read",
              tint = whatsAppReadCheck,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AttachmentPickerContent(
  onSelectFile: (fileName: String, fileSize: String, extension: String) -> Unit,
  onDismiss: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = "Send Original Files (No Compression)",
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White
    )
    Text(
      text = "Send ultra-large original files (up to 10 GB) with hardware Keystore AES-256 encryption.",
      fontSize = 12.sp,
      color = TextSecondary
    )

    // Grid of WhatsApp Attachment Options
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      AttachmentOption(
        title = "Document",
        subtitle = "Up to 10 GB",
        icon = Icons.Default.Description,
        color = Color(0xFF7F66FF),
        onClick = {
          onSelectFile("Complete_Project_Source_Code.zip", "1.65 GB", "ZIP")
        }
      )
      AttachmentOption(
        title = "Original Photo",
        subtitle = "RAW Uncompressed",
        icon = Icons.Default.Image,
        color = Color(0xFF00A884),
        onClick = {
          onSelectFile("Night_City_HDR_Lossless.RAW", "84.5 MB", "RAW")
        }
      )
      AttachmentOption(
        title = "4K Master",
        subtitle = "Zero downgrade",
        icon = Icons.Default.Movie,
        color = Color(0xFFE91E63),
        onClick = {
          onSelectFile("NeoTokyo_Cinematic_4K60.mov", "3.42 GB", "MOV")
        }
      )
      AttachmentOption(
        title = "Disk ISO",
        subtitle = "System Image",
        icon = Icons.Default.UploadFile,
        color = Color(0xFF0288D1),
        onClick = {
          onSelectFile("Zero_OS_Encrypted_Backup.iso", "7.80 GB", "ISO")
        }
      )
    }

    Spacer(modifier = Modifier.height(10.dp))
  }
}

@Composable
private fun AttachmentOption(
  title: String,
  subtitle: String,
  icon: ImageVector,
  color: Color,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(54.dp)
        .clip(CircleShape)
        .background(color),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = Color.White,
        modifier = Modifier.size(26.dp)
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    Text(text = subtitle, fontSize = 9.sp, color = TextMuted)
  }
}
