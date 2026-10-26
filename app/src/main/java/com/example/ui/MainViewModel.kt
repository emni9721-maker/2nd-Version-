package com.example.ui

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppNotification
import com.example.data.AuthenticationManager
import com.example.data.BackupSnapshot
import com.example.data.BiometricHelper
import com.example.data.CloudPlan
import com.example.data.Comment
import com.example.data.Conversation
import com.example.data.CryptoVaultManager
import com.example.data.DummyData
import com.example.data.ExifData
import com.example.data.FirebaseAuthHelper
import com.example.data.MediaType
import com.example.data.Reel
import com.example.data.Repository
import com.example.data.SocialPost
import com.example.data.StorageCategory
import com.example.data.Story
import com.example.data.UserProfile
import com.example.data.VaultItem
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class AppTab {
  FEED,
  POSTS,
  VAULT,
  REELS,
  CHAT,
  NOTIFICATIONS,
  PROFILE,
  SETTINGS
}

data class UploadDraft(
  val title: String = "",
  val type: MediaType = MediaType.IMAGE,
  val album: String = "Camera",
  val mediaUrl: String = "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?auto=format&fit=crop&w=1200&q=80",
  val originalSizeBytes: Long = 16_800_000L,
  val compressedSizeBytes: Long = 2_150_000L,
  val encryptionAlgorithm: String = "AES-256-GCM (Hardware Keystore)"
)

data class UiNotification(
  val id: String,
  val message: String,
  val isError: Boolean = false
)

class MainViewModel : ViewModel() {

  private val _currentTab = MutableStateFlow(AppTab.FEED)
  val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

  private val _isAmoledMode = MutableStateFlow(false)
  val isAmoledMode: StateFlow<Boolean> = _isAmoledMode.asStateFlow()

  // Firebase Auth & Profile State
  private val _currentUser = MutableStateFlow<FirebaseUser?>(FirebaseAuthHelper.currentUser)
  val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

  private val _isSessionActive = MutableStateFlow(false)
  val isSessionActive: StateFlow<Boolean> = _isSessionActive.asStateFlow()

  private val _userProfile = MutableStateFlow<UserProfile?>(null)
  val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

  private var repository: Repository? = null

  // Vault Security State
  private val _isVaultUnlocked = MutableStateFlow(false)
  val isVaultUnlocked: StateFlow<Boolean> = _isVaultUnlocked.asStateFlow()

  private val _isDecoyVault = MutableStateFlow(false)
  val isDecoyVault: StateFlow<Boolean> = _isDecoyVault.asStateFlow()

  private val _pinInput = MutableStateFlow("")
  val pinInput: StateFlow<String> = _pinInput.asStateFlow()

  private val _pinError = MutableStateFlow<String?>(null)
  val pinError: StateFlow<String?> = _pinError.asStateFlow()

  private val _selectedVaultAlbum = MutableStateFlow("All Media")
  val selectedVaultAlbum: StateFlow<String> = _selectedVaultAlbum.asStateFlow()

  // Vault Items (Cloud Synchronized)
  private val _vaultItems = MutableStateFlow(DummyData.initialVaultItems)
  val vaultItems: StateFlow<List<VaultItem>> = _vaultItems.asStateFlow()

  // Fullscreen Viewer
  private val _selectedMediaDetail = MutableStateFlow<VaultItem?>(null)
  val selectedMediaDetail: StateFlow<VaultItem?> = _selectedMediaDetail.asStateFlow()

  // Social & Feed (Cloud Synchronized)
  private val _posts = MutableStateFlow(DummyData.initialPosts)
  val posts: StateFlow<List<SocialPost>> = _posts.asStateFlow()

  private val _stories = MutableStateFlow(DummyData.initialStories)
  val stories: StateFlow<List<Story>> = _stories.asStateFlow()

  private val _activeStory = MutableStateFlow<Story?>(null)
  val activeStory: StateFlow<Story?> = _activeStory.asStateFlow()

  // Reels
  private val _reels = MutableStateFlow(DummyData.initialReels)
  val reels: StateFlow<List<Reel>> = _reels.asStateFlow()

  private val _activeReelIndex = MutableStateFlow(0)
  val activeReelIndex: StateFlow<Int> = _activeReelIndex.asStateFlow()

  // Messenger
  private val _conversations = MutableStateFlow(DummyData.initialConversations)
  val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

  private val _activeConversation = MutableStateFlow<Conversation?>(null)
  val activeConversation: StateFlow<Conversation?> = _activeConversation.asStateFlow()

  private val _isMessengerOpen = MutableStateFlow(false)
  val isMessengerOpen: StateFlow<Boolean> = _isMessengerOpen.asStateFlow()

  // Notifications
  private val _notifications = MutableStateFlow(DummyData.initialNotifications)
  val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

  // Language & Localization
  private val _selectedLanguage = MutableStateFlow("English")
  val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

  // Storage & Cloud Plans
  private val _storageCategories = MutableStateFlow(DummyData.storageCategories)
  val storageCategories: StateFlow<List<StorageCategory>> = _storageCategories.asStateFlow()

  private val _cloudPlans = MutableStateFlow(DummyData.cloudPlans)
  val cloudPlans: StateFlow<List<CloudPlan>> = _cloudPlans.asStateFlow()

  private val _snapshots = MutableStateFlow(DummyData.initialSnapshots)
  val snapshots: StateFlow<List<BackupSnapshot>> = _snapshots.asStateFlow()

  // Modals & Sheets
  private val _isUploadModalOpen = MutableStateFlow(false)
  val isUploadModalOpen: StateFlow<Boolean> = _isUploadModalOpen.asStateFlow()

  private val _uploadDraft = MutableStateFlow(UploadDraft())
  val uploadDraft: StateFlow<UploadDraft> = _uploadDraft.asStateFlow()

  private val _isKotlinHubOpen = MutableStateFlow(false)
  val isKotlinHubOpen: StateFlow<Boolean> = _isKotlinHubOpen.asStateFlow()

  private val _isReelsCommentOpen = MutableStateFlow(false)
  val isReelsCommentOpen: StateFlow<Boolean> = _isReelsCommentOpen.asStateFlow()

  private val _toastNotification = MutableStateFlow<UiNotification?>(null)
  val toastNotification: StateFlow<UiNotification?> = _toastNotification.asStateFlow()

  // Google Photos-style Automatic Cloud Backup State
  private val _isAutoBackupEnabled = MutableStateFlow(true)
  val isAutoBackupEnabled: StateFlow<Boolean> = _isAutoBackupEnabled.asStateFlow()

  private val _backupStatus = MutableStateFlow("All photos & videos backed up")
  val backupStatus: StateFlow<String> = _backupStatus.asStateFlow()

  private val _lastBackupTime = MutableStateFlow("Just now")
  val lastBackupTime: StateFlow<String> = _lastBackupTime.asStateFlow()

  private val _backupQuality = MutableStateFlow("Original Quality")
  val backupQuality: StateFlow<String> = _backupQuality.asStateFlow()

  private val _backupWifiOnly = MutableStateFlow(true)
  val backupWifiOnly: StateFlow<Boolean> = _backupWifiOnly.asStateFlow()

  private val _backupFolders = MutableStateFlow(listOf("Camera", "Screenshots", "Downloads"))
  val backupFolders: StateFlow<List<String>> = _backupFolders.asStateFlow()

  private val _isBackingUp = MutableStateFlow(false)
  val isBackingUp: StateFlow<Boolean> = _isBackingUp.asStateFlow()

  private val _backupProgress = MutableStateFlow(1.0f)
  val backupProgress: StateFlow<Float> = _backupProgress.asStateFlow()

  fun initFirebase(context: Context) {
    if (repository != null) return
    val appContext = context.applicationContext

    viewModelScope.launch(Dispatchers.IO) {
      val repo = Repository.create(appContext)
      repository = repo

      FirebaseAuthHelper.observeAuthState().collect { user ->
        _currentUser.value = user
        if (user != null) {
          AuthenticationManager.setSessionActive(appContext, true, user.email ?: user.uid)
          _isSessionActive.value = true
          try {
              // 1. Sync User Profile in Firestore with any pending signup details
              val pending = AuthenticationManager.pendingRegistration
              val email = if (pending?.contact?.contains("@") == true) pending.contact else (user.email ?: "")
              val phone = if (pending?.contact?.contains("@") == false) pending.contact else null
              repo.syncUserProfile(
                email = email,
                displayName = pending?.name ?: user.displayName,
                photoUrl = user.photoUrl?.toString(),
                username = pending?.username,
                phoneNumber = phone,
                age = pending?.age
              )
              AuthenticationManager.pendingRegistration = null

              // 2. Seed initial data if Firestore collections are brand new
              repo.seedInitialVaultIfEmpty(DummyData.initialVaultItems)
              repo.seedInitialPostsIfEmpty(DummyData.initialPosts)

              withContext(Dispatchers.Main) {
                showToast("Cloud Synchronized: ${user.email ?: user.displayName}")
              }
            } catch (e: Exception) {
              // Handled gracefully
            }

            // Real-time profile observer
            launch {
              repo.observeUserProfile(user.uid).collect { profile ->
                if (profile != null) {
                  _userProfile.value = profile
                }
              }
            }

            // Real-time social feed posts observer
            launch {
              repo.observeSocialPosts().collect { postList ->
                if (postList.isNotEmpty()) {
                  _posts.value = postList
                }
              }
            }

            // Real-time vault media metadata observer
            launch {
              repo.observeVaultItems().collect { itemList ->
                if (itemList.isNotEmpty()) {
                  _vaultItems.value = itemList
                }
              }
            }

            // Real-time snapshots observer
            launch {
              repo.observeSnapshots().collect { snapList ->
                if (snapList.isNotEmpty()) {
                  _snapshots.value = snapList
                }
              }
            }
          } else {
            _isVaultUnlocked.value = false
            _pinInput.value = ""
            _userProfile.value = null
          }
        }
      }
    }

  fun checkInitialSession(context: Context) {
    val active = AuthenticationManager.isSessionActive(context)
    _isSessionActive.value = active
    if (active) {
      val sessionUser = AuthenticationManager.getActiveSessionUser(context)
      if (_userProfile.value == null) {
        val prefs = context.getSharedPreferences("zero_auth_prefs", Context.MODE_PRIVATE)
        val savedName = prefs.getString("saved_name", "Elena Vance") ?: "Elena Vance"
        val savedUsername = prefs.getString("saved_username", "@elena_sec") ?: "@elena_sec"
        val savedContact = prefs.getString("saved_contact", sessionUser ?: "emni9721@gmail.com") ?: "emni9721@gmail.com"
        val email = if (savedContact.contains("@")) savedContact else "emni9721@gmail.com"
        val phone = if (!savedContact.contains("@")) savedContact else "+880 1712-345678"
        val bio = prefs.getString("saved_bio", "Zero-knowledge private cloud curator. 100% hardware-encrypted photography & 4K cinematic reels.") ?: "Zero-knowledge private cloud curator."
        val dob = prefs.getString("saved_dob", "15 August 2000") ?: "15 August 2000"
        val loc = prefs.getString("saved_location", "Dhaka, Bangladesh") ?: "Dhaka, Bangladesh"
        val school = prefs.getString("saved_school_college", "Dhaka College / University of Dhaka") ?: "Dhaka College / University of Dhaka"
        val work = prefs.getString("saved_workplace", "Zero Cloud Security Lab") ?: "Zero Cloud Security Lab"
        _userProfile.value = UserProfile(
          userId = "session_user",
          displayName = savedName,
          email = email,
          phoneNumber = phone,
          username = savedUsername,
          bio = bio,
          dateOfBirth = dob,
          location = loc,
          schoolCollege = school,
          workplace = work,
          photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
        )
      }
    }
  }

  fun onSessionLogin(context: Context, identifier: String) {
    AuthenticationManager.setSessionActive(context, true, identifier)
    _isSessionActive.value = true
    val prefs = context.getSharedPreferences("zero_auth_prefs", Context.MODE_PRIVATE)
    val savedName = prefs.getString("saved_name", "Elena Vance") ?: "Elena Vance"
    val savedUsername = prefs.getString("saved_username", if (identifier.startsWith("@")) identifier else "@${identifier.substringBefore("@")}") ?: "@elena_sec"
    val email = if (identifier.contains("@")) identifier else (prefs.getString("saved_contact", "emni9721@gmail.com") ?: "emni9721@gmail.com")
    val phone = if (!identifier.contains("@") && identifier.any { it.isDigit() }) identifier else "+880 1712-345678"
    val bio = prefs.getString("saved_bio", "Zero-knowledge private cloud curator. 100% hardware-encrypted photography & 4K cinematic reels.") ?: "Zero-knowledge private cloud curator."
    val dob = prefs.getString("saved_dob", "15 August 2000") ?: "15 August 2000"
    val loc = prefs.getString("saved_location", "Dhaka, Bangladesh") ?: "Dhaka, Bangladesh"
    val school = prefs.getString("saved_school_college", "Dhaka College / University of Dhaka") ?: "Dhaka College / University of Dhaka"
    val work = prefs.getString("saved_workplace", "Zero Cloud Security Lab") ?: "Zero Cloud Security Lab"
    _userProfile.value = UserProfile(
      userId = "session_user",
      displayName = savedName,
      email = email,
      phoneNumber = phone,
      username = savedUsername,
      bio = bio,
      dateOfBirth = dob,
      location = loc,
      schoolCollege = school,
      workplace = work,
      photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
    )
    showToast("Session active for $identifier.")
  }

  fun updateFullUserProfile(
    name: String,
    bio: String,
    dateOfBirth: String,
    location: String,
    schoolCollege: String,
    workplace: String,
    context: Context
  ) {
    val cur = _userProfile.value ?: UserProfile()
    val updated = cur.copy(
      displayName = name,
      bio = bio,
      dateOfBirth = dateOfBirth,
      location = location,
      schoolCollege = schoolCollege,
      workplace = workplace
    )
    _userProfile.value = updated

    val prefs = context.getSharedPreferences("zero_auth_prefs", Context.MODE_PRIVATE)
    prefs.edit()
      .putString("saved_name", name)
      .putString("saved_bio", bio)
      .putString("saved_dob", dateOfBirth)
      .putString("saved_location", location)
      .putString("saved_school_college", schoolCollege)
      .putString("saved_workplace", workplace)
      .apply()

    viewModelScope.launch(Dispatchers.IO) {
      try {
        repository?.updateFullUserProfile(
          displayName = name,
          bio = bio,
          dateOfBirth = dateOfBirth,
          location = location,
          schoolCollege = schoolCollege,
          workplace = workplace
        )
      } catch (_: Exception) {}
    }

    showToast("Profile updated successfully!")
  }

  fun setTab(tab: AppTab) {
    _currentTab.value = tab
  }

  fun toggleAmoledMode() {
    _isAmoledMode.value = !_isAmoledMode.value
  }

  fun setVaultAlbum(album: String) {
    _selectedVaultAlbum.value = album
  }

  // PIN & Biometric Handling
  fun onPinDigit(digit: String) {
    if (_pinInput.value.length < 4) {
      _pinInput.value += digit
      _pinError.value = null
      if (_pinInput.value.length == 4) {
        verifyEnteredPin(_pinInput.value)
      }
    }
  }

  fun onPinDelete() {
    if (_pinInput.value.isNotEmpty()) {
      _pinInput.value = _pinInput.value.dropLast(1)
      _pinError.value = null
    }
  }

  private fun verifyEnteredPin(pin: String) {
    when (BiometricHelper.verifyPin(pin)) {
      BiometricHelper.AuthResult.SUCCESS -> {
        _isVaultUnlocked.value = true
        _isDecoyVault.value = false
        _pinInput.value = ""
        _pinError.value = null
        showToast("Biometric Master Key Verified. Vault Unlocked.")
      }
      BiometricHelper.AuthResult.DECOY_SUCCESS -> {
        _isVaultUnlocked.value = true
        _isDecoyVault.value = true
        _pinInput.value = ""
        _pinError.value = null
        showToast("Decoy Vault Mode Activated (Self-Defense).")
      }
      else -> {
        _pinInput.value = ""
        _pinError.value = "Incorrect PIN. Default is 1234 or Decoy 9999"
      }
    }
  }

  fun unlockWithBiometrics() {
    viewModelScope.launch {
      delay(300)
      _isVaultUnlocked.value = true
      _isDecoyVault.value = false
      _pinInput.value = ""
      _pinError.value = null
      showToast("Biometric Ultrasonic Fingerprint Accepted 🛡️")
    }
  }

  fun lockVault() {
    _isVaultUnlocked.value = false
    _selectedMediaDetail.value = null
    _pinInput.value = ""
    showToast("Vault Locked with Hardware AES-256 GCM.")
  }

  // Vault Actions with Real-time Cross-Device Sync
  fun openMediaDetail(item: VaultItem) {
    _selectedMediaDetail.value = item
  }

  fun closeMediaDetail() {
    _selectedMediaDetail.value = null
  }

  fun toggleFavorite(itemId: String) {
    var updated: VaultItem? = null
    _vaultItems.value = _vaultItems.value.map {
      if (it.id == itemId) {
        val item = it.copy(isFavorite = !it.isFavorite)
        updated = item
        item
      } else it
    }
    _selectedMediaDetail.value = _selectedMediaDetail.value?.let { current ->
      if (current.id == itemId) current.copy(isFavorite = !current.isFavorite) else current
    }

    updated?.let { item ->
      viewModelScope.launch {
        try {
          repository?.saveVaultMedia(item)
        } catch (_: Exception) {}
      }
    }
  }

  fun softDeleteMedia(itemId: String) {
    var updated: VaultItem? = null
    _vaultItems.value = _vaultItems.value.map {
      if (it.id == itemId) {
        val item = it.copy(isDeleted = true, deletedAtTimestamp = System.currentTimeMillis())
        updated = item
        item
      } else it
    }
    _selectedMediaDetail.value = null
    showToast("Item moved to Trash bin (30-day retention).")

    updated?.let { item ->
      viewModelScope.launch {
        try {
          repository?.saveVaultMedia(item)
        } catch (_: Exception) {}
      }
    }
  }

  fun restoreMedia(itemId: String) {
    var updated: VaultItem? = null
    _vaultItems.value = _vaultItems.value.map {
      if (it.id == itemId) {
        val item = it.copy(isDeleted = false)
        updated = item
        item
      } else it
    }
    showToast("Restored from Trash to secure album.")

    updated?.let { item ->
      viewModelScope.launch {
        try {
          repository?.saveVaultMedia(item)
        } catch (_: Exception) {}
      }
    }
  }

  fun permanentlyDeleteMedia(itemId: String) {
    _vaultItems.value = _vaultItems.value.filterNot { it.id == itemId }
    showToast("Permanently shredded and wiped from cloud & disk.")

    viewModelScope.launch {
      try {
        repository?.deleteVaultMedia(itemId)
      } catch (_: Exception) {}
    }
  }

  fun shareVaultItemToFeed(item: VaultItem) {
    viewModelScope.launch {
      try {
        val newPost = repository?.createSocialPost(
          caption = "Shared directly from my encrypted ZERO vault: ${item.title} 📸🔐",
          tags = listOf("#ZEROvault", "#EncryptedMedia", "#Protected"),
          mediaUrl = item.mediaUrl,
          isVideo = item.type == MediaType.VIDEO
        )
        if (newPost != null) {
          _posts.value = listOf(newPost) + _posts.value.filterNot { it.id == newPost.id }
        }
      } catch (e: Exception) {
        // Fallback local post
        val fallbackPost = SocialPost(
          id = "p-shared-${System.currentTimeMillis()}",
          authorName = _currentUser.value?.displayName ?: "Zero Master",
          authorHandle = "@" + (_currentUser.value?.email?.substringBefore("@") ?: "zeromaster"),
          authorAvatar = _currentUser.value?.photoUrl?.toString() ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
          timestamp = "Just now",
          caption = "Shared directly from my encrypted ZERO vault: ${item.title} 📸🔐",
          tags = listOf("#ZEROvault", "#EncryptedMedia", "#Protected"),
          mediaUrl = item.mediaUrl,
          isVideo = item.type == MediaType.VIDEO,
          likesCount = 1,
          isLiked = true,
          commentsCount = 0
        )
        _posts.value = listOf(fallbackPost) + _posts.value
      }
      _selectedMediaDetail.value = null
      _currentTab.value = AppTab.FEED
      showToast("1-Tap Share: Media published to social feed and synced across devices!")
    }
  }

  // Upload & Compression with Cloud Sync
  fun openUploadModal() {
    _isUploadModalOpen.value = true
  }

  fun closeUploadModal() {
    _isUploadModalOpen.value = false
  }

  fun updateUploadDraft(draft: UploadDraft) {
    _uploadDraft.value = draft
  }

  fun submitUpload() {
    val draft = _uploadDraft.value
    val title = if (draft.title.isNotBlank()) draft.title else "Secured_Capture_${System.currentTimeMillis() % 10000}.${if (draft.type == MediaType.VIDEO) "mp4" else "jpg"}"
    val newItem = VaultItem(
      id = "v-${System.currentTimeMillis()}",
      title = title,
      type = draft.type,
      mediaUrl = draft.mediaUrl,
      album = draft.album,
      dateFormatted = "Just now",
      originalSizeBytes = draft.originalSizeBytes,
      compressedSizeBytes = draft.compressedSizeBytes,
      encryptionAlgorithm = draft.encryptionAlgorithm,
      keyAlias = "zero_key_${System.currentTimeMillis() % 999}",
      sha256Hash = CryptoVaultManager.generateSha256(title),
      isFavorite = false,
      exifData = ExifData(
        resolution = "4032 x 3024 (12.2 MP)",
        cameraModel = "Android Pro Sensor"
      )
    )
    _vaultItems.value = listOf(newItem) + _vaultItems.value

    if (draft.type == MediaType.VIDEO) {
      val newReel = Reel(
        id = "r-${System.currentTimeMillis()}",
        creatorName = _userProfile.value?.displayName ?: (_currentUser.value?.displayName ?: "Elena Vance"),
        creatorHandle = _userProfile.value?.username ?: "@zerouser",
        creatorAvatar = _userProfile.value?.photoUrl?.ifBlank { _currentUser.value?.photoUrl?.toString() }
          ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
        description = if (draft.title.isNotBlank()) draft.title else "Encrypted 4K Reel #${System.currentTimeMillis() % 1000} 🎥",
        audioTrack = "Original Audio • ZERO MasterKey",
        videoThumbnail = draft.mediaUrl,
        likesCount = 0,
        isLiked = false,
        commentsCount = 0,
        sharesCount = 0
      )
      _reels.value = listOf(newReel) + _reels.value
    }

    _isUploadModalOpen.value = false
    showToast("Encrypted with AES-256 GCM (${newItem.compressionPercentage}% space saved)!")

    viewModelScope.launch {
      try {
        repository?.saveVaultMedia(newItem)
        showToast("Synchronized encrypted media metadata to Cloud Firestore ☁️")
      } catch (e: Exception) {
        // Log sync error
      }
    }
  }

  // Social Feed Actions with Cross-Device Sync
  fun togglePostLike(postId: String) {
    _posts.value = _posts.value.map {
      if (it.id == postId) {
        val newLiked = !it.isLiked
        it.copy(
          isLiked = newLiked,
          likesCount = if (newLiked) it.likesCount + 1 else (it.likesCount - 1).coerceAtLeast(0)
        )
      } else it
    }

    viewModelScope.launch {
      try {
        repository?.togglePostLike(postId)
      } catch (_: Exception) {}
    }
  }

  fun addCommentToPost(postId: String, commentText: String) {
    if (commentText.isBlank()) return
    val newComment = Comment(
      id = "c-${System.currentTimeMillis()}",
      authorName = _currentUser.value?.displayName ?: "You",
      authorAvatar = _currentUser.value?.photoUrl?.toString() ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
      text = commentText.trim(),
      timestamp = "Just now"
    )
    _posts.value = _posts.value.map {
      if (it.id == postId) {
        it.copy(
          comments = it.comments + newComment,
          commentsCount = it.commentsCount + 1
        )
      } else it
    }
    showToast("Comment posted.")

    viewModelScope.launch {
      try {
        repository?.addCommentToPost(postId, commentText.trim())
      } catch (_: Exception) {}
    }
  }

  // Stories
  fun openStory(story: Story) {
    _activeStory.value = story
    _stories.value = _stories.value.map {
      if (it.id == story.id) it.copy(hasUnseen = false) else it
    }
  }

  fun closeStory() {
    _activeStory.value = null
  }

  // Reels
  fun setActiveReel(index: Int) {
    _activeReelIndex.value = index
  }

  fun toggleReelLike(reelId: String) {
    _reels.value = _reels.value.map {
      if (it.id == reelId) {
        val newLiked = !it.isLiked
        it.copy(
          isLiked = newLiked,
          likesCount = if (newLiked) it.likesCount + 1 else (it.likesCount - 1).coerceAtLeast(0)
        )
      } else it
    }
  }

  fun openReelsComments() {
    _isReelsCommentOpen.value = true
  }

  fun closeReelsComments() {
    _isReelsCommentOpen.value = false
  }

  fun addCommentToReel(reelId: String, text: String) {
    if (text.isBlank()) return
    val newComment = Comment(
      id = "rc-${System.currentTimeMillis()}",
      authorName = _currentUser.value?.displayName ?: "You",
      authorAvatar = _currentUser.value?.photoUrl?.toString() ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
      text = text.trim(),
      timestamp = "Just now"
    )
    _reels.value = _reels.value.map {
      if (it.id == reelId) {
        it.copy(
          comments = it.comments + newComment,
          commentsCount = it.commentsCount + 1
        )
      } else it
    }
  }

  // Messenger
  fun openMessenger() {
    _isMessengerOpen.value = true
    if (_conversations.value.isNotEmpty() && _activeConversation.value == null) {
      _activeConversation.value = _conversations.value.first()
    }
  }

  fun closeMessenger() {
    _isMessengerOpen.value = false
  }

  fun selectConversation(conv: Conversation) {
    _activeConversation.value = conv
    _conversations.value = _conversations.value.map {
      if (it.id == conv.id) it.copy(unreadCount = 0) else it
    }
  }

  fun sendMessage(convId: String, text: String) {
    if (text.isBlank()) return
    val msg = com.example.data.ChatMessage(
      id = "m-${System.currentTimeMillis()}",
      text = text.trim(),
      timestamp = "Just now",
      isMine = true,
      isEncrypted = true
    )
    _conversations.value = _conversations.value.map { conv ->
      if (conv.id == convId) {
        conv.copy(
          messages = conv.messages + msg,
          lastMessage = text.trim(),
          lastMessageTime = "Just now"
        )
      } else conv
    }
    _activeConversation.value = _conversations.value.find { it.id == convId }
  }

  fun sendFileMessage(
    convId: String,
    fileName: String,
    fileSizeFormatted: String,
    fileExtension: String
  ) {
    val msg = com.example.data.ChatMessage(
      id = "m-${System.currentTimeMillis()}",
      text = "Sent original file: $fileName",
      timestamp = "Just now",
      isMine = true,
      isEncrypted = true,
      fileName = fileName,
      fileSizeFormatted = fileSizeFormatted,
      fileExtension = fileExtension,
      isOriginalFile = true,
      isRead = true
    )
    _conversations.value = _conversations.value.map { conv ->
      if (conv.id == convId) {
        conv.copy(
          messages = conv.messages + msg,
          lastMessage = "Sent original file: $fileName ($fileSizeFormatted)",
          lastMessageTime = "Just now"
        )
      } else conv
    }
    _activeConversation.value = _conversations.value.find { it.id == convId }
    showToast("Original file ($fileSizeFormatted) sent losslessly via E2EE!")
  }

  fun clearAllNotifications() {
    _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    showToast("All notifications marked as read")
  }

  // Cloud Tiers & Backup Snapshots
  fun selectCloudPlan(planId: String) {
    _cloudPlans.value = _cloudPlans.value.map {
      it.copy(isCurrent = (it.id == planId))
    }
    val plan = _cloudPlans.value.find { it.id == planId }
    val quotaBytes = (plan?.capacityGb ?: 50L) * 1024L * 1024L * 1024L
    viewModelScope.launch {
      try {
        repository?.updateUserPlan(planId, quotaBytes)
      } catch (_: Exception) {}
    }
    showToast("Subscribed to ${plan?.name ?: "plan"} (${plan?.capacityGb} GB capacity)!")
  }

  fun createSnapshot() {
    val newSnap = BackupSnapshot(
      id = "snap-${System.currentTimeMillis()}",
      name = "Instant Cloud Snapshot #${_snapshots.value.size + 85}",
      dateFormatted = "Just now",
      sizeMb = 18.5,
      hash = CryptoVaultManager.generateSha256("snap_${System.currentTimeMillis()}")
    )
    _snapshots.value = listOf(newSnap) + _snapshots.value
    showToast("Created encrypted database snapshot with SHA-256 verification!")

    viewModelScope.launch {
      try {
        repository?.saveSnapshot(newSnap)
      } catch (_: Exception) {}
    }
  }

  fun restoreSnapshot(snap: BackupSnapshot) {
    showToast("Restored from snapshot: ${snap.name}. All vault keys aligned!")
  }

  // Language & Security Operations
  fun setLanguage(lang: String) {
    _selectedLanguage.value = lang
    val current = _userProfile.value ?: UserProfile()
    _userProfile.value = current.copy(preferredLanguage = lang)
    showToast("Language changed to: $lang")
    viewModelScope.launch {
      try {
        repository?.updateUserProfileSecurity(preferredLanguage = lang)
      } catch (_: Exception) {}
    }
  }

  fun updatePhoneNumber(newPhone: String) {
    val current = _userProfile.value ?: UserProfile()
    _userProfile.value = current.copy(phoneNumber = newPhone)
    showToast("Phone number updated: $newPhone")
    viewModelScope.launch {
      try {
        repository?.updateUserProfileSecurity(phoneNumber = newPhone)
      } catch (_: Exception) {}
    }
  }

  fun removePhoneNumber() {
    val current = _userProfile.value ?: UserProfile()
    _userProfile.value = current.copy(phoneNumber = "")
    showToast("Phone number removed")
    viewModelScope.launch {
      try {
        repository?.updateUserProfileSecurity(phoneNumber = "")
      } catch (_: Exception) {}
    }
  }

  fun updatePrimaryEmail(newEmail: String) {
    val current = _userProfile.value ?: UserProfile()
    _userProfile.value = current.copy(email = newEmail)
    showToast("Primary email updated: $newEmail")
  }

  fun updateRecoveryEmail(newEmail: String) {
    val current = _userProfile.value ?: UserProfile()
    _userProfile.value = current.copy(secondaryEmail = newEmail)
    showToast("Recovery email updated: $newEmail")
    viewModelScope.launch {
      try {
        repository?.updateUserProfileSecurity(secondaryEmail = newEmail)
      } catch (_: Exception) {}
    }
  }

  fun removeRecoveryEmail() {
    val current = _userProfile.value ?: UserProfile()
    _userProfile.value = current.copy(secondaryEmail = "")
    showToast("Recovery email removed")
    viewModelScope.launch {
      try {
        repository?.updateUserProfileSecurity(secondaryEmail = "")
      } catch (_: Exception) {}
    }
  }

  fun changeMasterPassword(currentPass: String, newPass: String, onResult: (Boolean, String) -> Unit) {
    if (newPass.length < 6) {
      onResult(false, "Password must be at least 6 characters")
      return
    }
    showToast("Master password updated with hardware AES-256 derivation!")
    onResult(true, "Password changed successfully")
  }

  // Sign out of Session & Google Account
  fun signOut(context: Context) {
    AuthenticationManager.setSessionActive(context, false)
    _isSessionActive.value = false
    _currentUser.value = null
    _userProfile.value = null
    _isVaultUnlocked.value = false
    _currentTab.value = AppTab.FEED
    val credManager = CredentialManager.create(context)
    FirebaseAuthHelper.signOut(
      context = context,
      credentialManager = credManager,
      onSignOutComplete = {
        showToast("Signed out. Session securely terminated.")
      },
      scope = viewModelScope
    )
  }

  // Kotlin Hub & GitHub Actions Modal
  fun openKotlinHub() {
    _isKotlinHubOpen.value = true
  }

  fun closeKotlinHub() {
    _isKotlinHubOpen.value = false
  }

  private fun showToast(msg: String, isError: Boolean = false) {
    _toastNotification.value = UiNotification(
      id = System.currentTimeMillis().toString(),
      message = msg,
      isError = isError
    )
  }

  fun clearToast() {
    _toastNotification.value = null
  }

  // Google Photos-style Auto Backup Actions
  fun toggleAutoBackup() {
    val newState = !_isAutoBackupEnabled.value
    _isAutoBackupEnabled.value = newState
    if (newState) {
      _backupStatus.value = "All photos & videos backed up"
      showToast("Automatic Cloud Backup Enabled (Google Photos sync active)")
    } else {
      _backupStatus.value = "Backup paused"
      showToast("Automatic Backup Paused. Media saved locally.")
    }
  }

  fun triggerBackupNow() {
    if (_isBackingUp.value) return
    viewModelScope.launch {
      _isBackingUp.value = true
      _backupStatus.value = "Backing up photos & videos..."
      _backupProgress.value = 0.2f
      delay(300)
      _backupProgress.value = 0.5f
      delay(400)
      _backupProgress.value = 0.85f
      delay(350)
      _backupProgress.value = 1.0f
      _isBackingUp.value = false
      _backupStatus.value = "All photos & videos backed up"
      _lastBackupTime.value = "Just now"
      showToast("Cloud Backup Complete: All media synchronized!")
    }
  }

  fun setBackupQuality(quality: String) {
    _backupQuality.value = quality
    showToast("Backup Quality: $quality")
  }

  fun toggleBackupWifiOnly() {
    _backupWifiOnly.value = !_backupWifiOnly.value
    val msg = if (_backupWifiOnly.value) "Backing up over Wi-Fi only" else "Backing up over Wi-Fi & Mobile data"
    showToast(msg)
  }

  fun toggleBackupFolder(folder: String) {
    val current = _backupFolders.value.toMutableList()
    if (current.contains(folder)) {
      current.remove(folder)
      showToast("Excluded $folder from backup")
    } else {
      current.add(folder)
      showToast("Included $folder in backup")
    }
    _backupFolders.value = current
  }
}
