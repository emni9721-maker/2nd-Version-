package com.example.data

enum class MediaType {
  IMAGE, VIDEO
}

data class ExifData(
  val resolution: String = "4032 x 3024 (12.2 MP)",
  val iso: String = "ISO 80",
  val aperture: String = "f/1.7",
  val shutterSpeed: String = "1/120s",
  val focalLength: String = "24mm eq.",
  val cameraModel: String = "Android Pro Sensor HDR"
)

data class VaultItem(
  val id: String,
  val title: String,
  val type: MediaType,
  val mediaUrl: String,
  val album: String, // "Camera", "Wallpapers", "Secret Vault", "Downloads"
  val dateFormatted: String,
  val originalSizeBytes: Long,
  val compressedSizeBytes: Long,
  val encryptionAlgorithm: String = "AES-256-GCM (Hardware Keystore)",
  val keyAlias: String = "alias_zero_master_2026",
  val sha256Hash: String = "8f4e2c...b19d",
  val isFavorite: Boolean = false,
  val isDeleted: Boolean = false,
  val deletedAtTimestamp: Long = 0L,
  val exifData: ExifData = ExifData(),
  val videoDurationSec: Int = 0
) {
  val compressionPercentage: Int
    get() {
      if (originalSizeBytes <= 0) return 0
      val saved = originalSizeBytes - compressedSizeBytes
      return ((saved.toDouble() / originalSizeBytes.toDouble()) * 100).toInt().coerceAtLeast(0)
    }

  val formattedCompressedSize: String
    get() {
      val mb = compressedSizeBytes.toDouble() / (1024 * 1024)
      return String.format("%.1f MB", mb)
    }

  val formattedOriginalSize: String
    get() {
      val mb = originalSizeBytes.toDouble() / (1024 * 1024)
      return String.format("%.1f MB", mb)
    }
}

data class Comment(
  val id: String,
  val authorName: String,
  val authorAvatar: String,
  val text: String,
  val timestamp: String
)

data class SocialPost(
  val id: String,
  val authorName: String,
  val authorHandle: String,
  val authorAvatar: String,
  val timestamp: String,
  val caption: String,
  val tags: List<String> = emptyList(),
  val mediaUrl: String,
  val isVideo: Boolean = false,
  val likesCount: Int = 0,
  val isLiked: Boolean = false,
  val commentsCount: Int = 0,
  val comments: List<Comment> = emptyList(),
  val sharesCount: Int = 0
)

data class Story(
  val id: String,
  val authorName: String,
  val authorAvatar: String,
  val mediaUrl: String,
  val timestamp: String,
  val hasUnseen: Boolean = true
)

data class Reel(
  val id: String,
  val creatorName: String,
  val creatorHandle: String,
  val creatorAvatar: String,
  val description: String,
  val audioTrack: String,
  val videoThumbnail: String,
  val likesCount: Int,
  val isLiked: Boolean,
  val commentsCount: Int,
  val sharesCount: Int,
  val comments: List<Comment> = emptyList()
)

data class ChatMessage(
  val id: String,
  val text: String,
  val timestamp: String,
  val isMine: Boolean,
  val isEncrypted: Boolean = true,
  val fileName: String? = null,
  val fileSizeFormatted: String? = null,
  val fileExtension: String? = null,
  val isOriginalFile: Boolean = false,
  val isRead: Boolean = true
)

data class AppNotification(
  val id: String,
  val title: String,
  val description: String,
  val time: String,
  val iconType: String = "CLOUD", // CLOUD, BACKUP, VAULT, SOCIAL, FILE
  val isRead: Boolean = false,
  val avatarUrl: String? = null
)

data class Conversation(
  val id: String,
  val participantName: String,
  val participantHandle: String,
  val participantAvatar: String,
  val isOnline: Boolean,
  val lastMessage: String,
  val lastMessageTime: String,
  val unreadCount: Int = 0,
  val messages: List<ChatMessage> = emptyList()
)

data class StorageCategory(
  val name: String,
  val sizeGb: Double,
  val percentage: Float
)

data class CloudPlan(
  val id: String,
  val name: String,
  val capacityGb: Long,
  val priceMonthly: String,
  val isCurrent: Boolean = false,
  val badge: String? = null,
  val features: List<String>
)

data class BackupSnapshot(
  val id: String,
  val name: String,
  val dateFormatted: String,
  val sizeMb: Double,
  val hash: String,
  val status: String = "Encrypted (AES-GCM)"
)
