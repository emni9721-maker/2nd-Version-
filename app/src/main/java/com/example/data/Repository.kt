package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class UserProfile(
  val userId: String = "",
  val email: String = "",
  val displayName: String = "",
  val photoUrl: String = "",
  val bio: String = "Zero-knowledge private cloud curator. 100% hardware-encrypted photography & 4K cinematic reels.",
  val followersCount: Long = 1420L,
  val followingCount: Long = 342L,
  val storageUsedBytes: Long = 19_756_849_152L, // 18.4 GB
  val storageQuotaBytes: Long = 53_687_091_200L, // 50 GB
  val planId: String = "tier-starter",
  val lastSyncedAt: Timestamp? = null
)

/**
 * Repository layer connecting ZERO to Firebase Firestore for cross-device
 * synchronization of User Profile data, Social Feed posts, and encrypted Media Metadata.
 */
class Repository(private val db: FirebaseFirestore) {

  companion object {
    private const val TAG = "ZeroRepository"

    fun create(context: Context): Repository {
      val databaseId = context.getString(R.string.firestore_database_id)
      val firestore = FirebaseFirestore.getInstance(databaseId)
      return Repository(firestore)
    }
  }

  private val auth get() = Firebase.auth

  private fun requireUserId(): String {
    return auth.currentUser?.uid
      ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
  }

  // =========================================================================
  // 1. User Profile Synchronization
  // =========================================================================

  suspend fun syncUserProfile(email: String?, displayName: String?, photoUrl: String?) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid)
    val existing = docRef.get().await()

    val payload = mutableMapOf<String, Any>(
      "userId" to uid,
      "email" to (email ?: ""),
      "displayName" to (displayName ?: "Zero Master"),
      "photoUrl" to (photoUrl ?: ""),
      "lastSyncedAt" to FieldValue.serverTimestamp(),
      "updatedAt" to FieldValue.serverTimestamp()
    )

    if (!existing.exists()) {
      payload["createdAt"] = FieldValue.serverTimestamp()
      payload["bio"] = "Zero-knowledge private cloud curator. 100% hardware-encrypted photography & 4K cinematic reels."
      payload["followersCount"] = 1420L
      payload["followingCount"] = 342L
      payload["storageUsedBytes"] = 19_756_849_152L
      payload["storageQuotaBytes"] = 53_687_091_200L
      payload["planId"] = "tier-starter"
    }

    docRef.set(payload, SetOptions.merge()).await()
    Log.d(TAG, "Synchronized profile data for user: $uid ($email)")
  }

  fun observeUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
    val docRef = db.collection("users").document(userId)
    val listener = docRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing profile $userId", error)
        return@addSnapshotListener
      }
      if (snapshot != null && snapshot.exists()) {
        val profile = UserProfile(
          userId = snapshot.getString("userId") ?: userId,
          email = snapshot.getString("email") ?: "",
          displayName = snapshot.getString("displayName") ?: "Zero Master",
          photoUrl = snapshot.getString("photoUrl") ?: "",
          bio = snapshot.getString("bio") ?: "",
          followersCount = snapshot.getLong("followersCount") ?: 1420L,
          followingCount = snapshot.getLong("followingCount") ?: 342L,
          storageUsedBytes = snapshot.getLong("storageUsedBytes") ?: 19_756_849_152L,
          storageQuotaBytes = snapshot.getLong("storageQuotaBytes") ?: 53_687_091_200L,
          planId = snapshot.getString("planId") ?: "tier-starter",
          lastSyncedAt = snapshot.getTimestamp("lastSyncedAt")
        )
        trySend(profile)
      } else {
        trySend(null)
      }
    }
    awaitClose { listener.remove() }
  }

  suspend fun updateUserBio(bio: String) {
    val uid = requireUserId()
    db.collection("users").document(uid).update(
      mapOf(
        "bio" to bio,
        "updatedAt" to FieldValue.serverTimestamp()
      )
    ).await()
  }

  suspend fun updateUserPlan(planId: String, quotaBytes: Long) {
    val uid = requireUserId()
    db.collection("users").document(uid).update(
      mapOf(
        "planId" to planId,
        "storageQuotaBytes" to quotaBytes,
        "updatedAt" to FieldValue.serverTimestamp()
      )
    ).await()
  }

  // =========================================================================
  // 2. Social Feed Posts & Comments Synchronization
  // =========================================================================

  fun observeSocialPosts(): Flow<List<SocialPost>> = callbackFlow {
    val collectionRef = db.collection("posts")
      .orderBy("createdAt", Query.Direction.DESCENDING)

    val listener = collectionRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing social posts", error)
        return@addSnapshotListener
      }
      if (snapshot != null) {
        val currentUid = auth.currentUser?.uid ?: ""
        val posts = snapshot.documents.mapNotNull { doc ->
          try {
            val authorName = doc.getString("authorName") ?: "Anonymous"
            val authorHandle = doc.getString("authorHandle") ?: "@zerouser"
            val authorAvatar = doc.getString("authorAvatar") ?: ""
            val timestamp = doc.getString("timestamp") ?: "Recently"
            val caption = doc.getString("caption") ?: ""
            @Suppress("UNCHECKED_CAST")
            val tags = (doc.get("tags") as? List<String>) ?: emptyList()
            val mediaUrl = doc.getString("mediaUrl") ?: ""
            val isVideo = doc.getBoolean("isVideo") ?: false
            val likesCount = (doc.getLong("likesCount") ?: 0L).toInt()
            @Suppress("UNCHECKED_CAST")
            val likedUserIds = (doc.get("likedUserIds") as? List<String>) ?: emptyList()
            val isLiked = likedUserIds.contains(currentUid)
            val commentsCount = (doc.getLong("commentsCount") ?: 0L).toInt()
            val sharesCount = (doc.getLong("sharesCount") ?: 0L).toInt()

            SocialPost(
              id = doc.id,
              authorName = authorName,
              authorHandle = authorHandle,
              authorAvatar = authorAvatar,
              timestamp = timestamp,
              caption = caption,
              tags = tags,
              mediaUrl = mediaUrl,
              isVideo = isVideo,
              likesCount = likesCount,
              isLiked = isLiked,
              commentsCount = commentsCount,
              sharesCount = sharesCount
            )
          } catch (e: Exception) {
            Log.w(TAG, "Failed to parse post ${doc.id}", e)
            null
          }
        }
        trySend(posts)
      }
    }
    awaitClose { listener.remove() }
  }

  suspend fun createSocialPost(
    caption: String,
    tags: List<String>,
    mediaUrl: String,
    isVideo: Boolean
  ): SocialPost {
    val uid = requireUserId()
    val user = auth.currentUser
    val docRef = db.collection("posts").document()

    val authorName = user?.displayName ?: "Zero Master"
    val authorHandle = "@" + (user?.email?.substringBefore("@") ?: "zeromaster")
    val authorAvatar = user?.photoUrl?.toString() ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
    val timestamp = "Just now"

    val payload = mapOf(
      "id" to docRef.id,
      "authorId" to uid,
      "authorName" to authorName,
      "authorHandle" to authorHandle,
      "authorAvatar" to authorAvatar,
      "timestamp" to timestamp,
      "caption" to caption,
      "tags" to tags,
      "mediaUrl" to mediaUrl,
      "isVideo" to isVideo,
      "likesCount" to 0L,
      "likedUserIds" to emptyList<String>(),
      "commentsCount" to 0L,
      "sharesCount" to 0L,
      "createdAt" to FieldValue.serverTimestamp(),
      "updatedAt" to FieldValue.serverTimestamp()
    )

    docRef.set(payload).await()

    return SocialPost(
      id = docRef.id,
      authorName = authorName,
      authorHandle = authorHandle,
      authorAvatar = authorAvatar,
      timestamp = timestamp,
      caption = caption,
      tags = tags,
      mediaUrl = mediaUrl,
      isVideo = isVideo,
      likesCount = 0,
      isLiked = false,
      commentsCount = 0,
      sharesCount = 0
    )
  }

  suspend fun togglePostLike(postId: String) {
    val uid = requireUserId()
    val docRef = db.collection("posts").document(postId)
    val doc = docRef.get().await()
    if (!doc.exists()) return

    @Suppress("UNCHECKED_CAST")
    val likedUserIds = (doc.get("likedUserIds") as? List<String>)?.toMutableList() ?: mutableListOf()
    val isCurrentlyLiked = likedUserIds.contains(uid)

    val newLikesCount: Long
    if (isCurrentlyLiked) {
      likedUserIds.remove(uid)
      newLikesCount = ((doc.getLong("likesCount") ?: 1L) - 1L).coerceAtLeast(0L)
    } else {
      likedUserIds.add(uid)
      newLikesCount = (doc.getLong("likesCount") ?: 0L) + 1L
    }

    docRef.update(
      mapOf(
        "likedUserIds" to likedUserIds,
        "likesCount" to newLikesCount,
        "updatedAt" to FieldValue.serverTimestamp()
      )
    ).await()
  }

  suspend fun addCommentToPost(postId: String, text: String): Comment {
    val uid = requireUserId()
    val user = auth.currentUser
    val commentsRef = db.collection("posts").document(postId).collection("comments").document()

    val authorName = user?.displayName ?: "Zero Master"
    val authorAvatar = user?.photoUrl?.toString() ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
    val timestamp = "Just now"

    val payload = mapOf(
      "id" to commentsRef.id,
      "postId" to postId,
      "authorId" to uid,
      "authorName" to authorName,
      "authorAvatar" to authorAvatar,
      "text" to text,
      "createdAt" to FieldValue.serverTimestamp()
    )

    commentsRef.set(payload).await()

    // Increment commentsCount on parent post
    db.collection("posts").document(postId).update(
      mapOf(
        "commentsCount" to FieldValue.increment(1),
        "updatedAt" to FieldValue.serverTimestamp()
      )
    ).await()

    return Comment(
      id = commentsRef.id,
      authorName = authorName,
      authorAvatar = authorAvatar,
      text = text,
      timestamp = timestamp
    )
  }

  fun observePostComments(postId: String): Flow<List<Comment>> = callbackFlow {
    val collectionRef = db.collection("posts").document(postId).collection("comments")
      .orderBy("createdAt", Query.Direction.ASCENDING)

    val listener = collectionRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing comments for post $postId", error)
        return@addSnapshotListener
      }
      if (snapshot != null) {
        val comments = snapshot.documents.mapNotNull { doc ->
          try {
            Comment(
              id = doc.id,
              authorName = doc.getString("authorName") ?: "Anonymous",
              authorAvatar = doc.getString("authorAvatar") ?: "",
              text = doc.getString("text") ?: "",
              timestamp = "Recent"
            )
          } catch (e: Exception) {
            null
          }
        }
        trySend(comments)
      }
    }
    awaitClose { listener.remove() }
  }

  suspend fun seedInitialPostsIfEmpty(defaultPosts: List<SocialPost>) {
    val currentDocs = db.collection("posts").limit(1).get().await()
    if (currentDocs.isEmpty) {
      val uid = auth.currentUser?.uid ?: "system"
      for (post in defaultPosts) {
        val docRef = db.collection("posts").document(post.id)
        val payload = mapOf(
          "id" to post.id,
          "authorId" to uid,
          "authorName" to post.authorName,
          "authorHandle" to post.authorHandle,
          "authorAvatar" to post.authorAvatar,
          "timestamp" to post.timestamp,
          "caption" to post.caption,
          "tags" to post.tags,
          "mediaUrl" to post.mediaUrl,
          "isVideo" to post.isVideo,
          "likesCount" to post.likesCount.toLong(),
          "likedUserIds" to if (post.isLiked) listOf(uid) else emptyList<String>(),
          "commentsCount" to post.commentsCount.toLong(),
          "sharesCount" to post.sharesCount.toLong(),
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
      }
    }
  }

  // =========================================================================
  // 3. Encrypted Media Metadata (Vault Items) Synchronization
  // =========================================================================

  fun observeVaultItems(): Flow<List<VaultItem>> = callbackFlow {
    val uid = try {
      requireUserId()
    } catch (e: Exception) {
      close(e)
      return@callbackFlow
    }

    val collectionRef = db.collection("users").document(uid).collection("vault_items")
      .orderBy("updatedAt", Query.Direction.DESCENDING)

    val listener = collectionRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing vault items for user $uid", error)
        return@addSnapshotListener
      }
      if (snapshot != null) {
        val items = snapshot.documents.mapNotNull { doc ->
          try {
            val title = doc.getString("title") ?: "Secured Item"
            val typeStr = doc.getString("type") ?: "IMAGE"
            val type = if (typeStr == "VIDEO") MediaType.VIDEO else MediaType.IMAGE
            val mediaUrl = doc.getString("mediaUrl") ?: ""
            val album = doc.getString("album") ?: "Camera"
            val dateFormatted = doc.getString("dateFormatted") ?: "Cloud Synced"
            val originalSizeBytes = doc.getLong("originalSizeBytes") ?: 0L
            val compressedSizeBytes = doc.getLong("compressedSizeBytes") ?: 0L
            val encryptionAlgorithm = doc.getString("encryptionAlgorithm") ?: "AES-256-GCM (Hardware Keystore)"
            val keyAlias = doc.getString("keyAlias") ?: "zero_master_sec01"
            val sha256Hash = doc.getString("sha256Hash") ?: ""
            val isFavorite = doc.getBoolean("isFavorite") ?: false
            val isDeleted = doc.getBoolean("isDeleted") ?: false
            val deletedAtTimestamp = doc.getLong("deletedAtTimestamp") ?: 0L
            val videoDurationSec = (doc.getLong("videoDurationSec") ?: 0L).toInt()

            val exif = ExifData(
              resolution = doc.getString("resolution") ?: "4032 x 3024 (12.2 MP)",
              iso = doc.getString("iso") ?: "ISO 80",
              aperture = doc.getString("aperture") ?: "f/1.8",
              shutterSpeed = doc.getString("shutterSpeed") ?: "1/120s",
              cameraModel = doc.getString("cameraModel") ?: "Android Pro Sensor"
            )

            VaultItem(
              id = doc.id,
              title = title,
              type = type,
              mediaUrl = mediaUrl,
              album = album,
              dateFormatted = dateFormatted,
              originalSizeBytes = originalSizeBytes,
              compressedSizeBytes = compressedSizeBytes,
              encryptionAlgorithm = encryptionAlgorithm,
              keyAlias = keyAlias,
              sha256Hash = sha256Hash,
              isFavorite = isFavorite,
              isDeleted = isDeleted,
              deletedAtTimestamp = deletedAtTimestamp,
              exifData = exif,
              videoDurationSec = videoDurationSec
            )
          } catch (e: Exception) {
            Log.w(TAG, "Failed to parse vault item ${doc.id}", e)
            null
          }
        }
        trySend(items)
      }
    }

    awaitClose { listener.remove() }
  }

  suspend fun saveVaultMedia(item: VaultItem) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("vault_items").document(item.id)
    val payload = mapOf(
      "id" to item.id,
      "userId" to uid,
      "title" to item.title,
      "type" to item.type.name,
      "mediaUrl" to item.mediaUrl,
      "album" to item.album,
      "dateFormatted" to item.dateFormatted,
      "originalSizeBytes" to item.originalSizeBytes,
      "compressedSizeBytes" to item.compressedSizeBytes,
      "encryptionAlgorithm" to item.encryptionAlgorithm,
      "keyAlias" to item.keyAlias,
      "sha256Hash" to item.sha256Hash,
      "isFavorite" to item.isFavorite,
      "isDeleted" to item.isDeleted,
      "deletedAtTimestamp" to item.deletedAtTimestamp,
      "videoDurationSec" to item.videoDurationSec.toLong(),
      "resolution" to item.exifData.resolution,
      "iso" to item.exifData.iso,
      "aperture" to item.exifData.aperture,
      "shutterSpeed" to item.exifData.shutterSpeed,
      "cameraModel" to item.exifData.cameraModel,
      "updatedAt" to FieldValue.serverTimestamp()
    )
    docRef.set(payload, SetOptions.merge()).await()
    Log.d(TAG, "Saved encrypted vault media: ${item.id} (${item.title})")
  }

  suspend fun deleteVaultMedia(itemId: String) {
    val uid = requireUserId()
    db.collection("users").document(uid).collection("vault_items").document(itemId).delete().await()
    Log.d(TAG, "Deleted vault item from cloud: $itemId")
  }

  suspend fun seedInitialVaultIfEmpty(defaultItems: List<VaultItem>) {
    val uid = requireUserId()
    val collectionRef = db.collection("users").document(uid).collection("vault_items")
    val currentDocs = collectionRef.limit(1).get().await()
    if (currentDocs.isEmpty) {
      for (item in defaultItems) {
        saveVaultMedia(item)
      }
      Log.d(TAG, "Seeded initial encrypted items to user $uid's vault")
    }
  }

  // =========================================================================
  // 4. Backup Snapshots Synchronization
  // =========================================================================

  suspend fun saveSnapshot(snapshot: BackupSnapshot) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("snapshots").document(snapshot.id)
    val payload = mapOf(
      "id" to snapshot.id,
      "userId" to uid,
      "name" to snapshot.name,
      "dateFormatted" to snapshot.dateFormatted,
      "sizeMb" to snapshot.sizeMb,
      "hash" to snapshot.hash,
      "status" to snapshot.status,
      "createdAt" to FieldValue.serverTimestamp()
    )
    docRef.set(payload, SetOptions.merge()).await()
  }

  fun observeSnapshots(): Flow<List<BackupSnapshot>> = callbackFlow {
    val uid = try {
      requireUserId()
    } catch (e: Exception) {
      close(e)
      return@callbackFlow
    }

    val collectionRef = db.collection("users").document(uid).collection("snapshots")
      .orderBy("createdAt", Query.Direction.DESCENDING)

    val listener = collectionRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing snapshots", error)
        return@addSnapshotListener
      }
      if (snapshot != null) {
        val list = snapshot.documents.mapNotNull { doc ->
          try {
            BackupSnapshot(
              id = doc.id,
              name = doc.getString("name") ?: "Snapshot",
              dateFormatted = doc.getString("dateFormatted") ?: "Recent",
              sizeMb = doc.getDouble("sizeMb") ?: 18.0,
              hash = doc.getString("hash") ?: "",
              status = doc.getString("status") ?: "Encrypted (AES-GCM)"
            )
          } catch (e: Exception) {
            null
          }
        }
        trySend(list)
      }
    }
    awaitClose { listener.remove() }
  }
}
